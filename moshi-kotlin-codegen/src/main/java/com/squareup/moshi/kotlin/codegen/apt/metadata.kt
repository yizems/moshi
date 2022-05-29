/*
 * Copyright (C) 2018 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.moshi.kotlin.codegen.apt

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.isAbstract
import com.squareup.kotlinpoet.metadata.isClass
import com.squareup.kotlinpoet.metadata.isEnum
import com.squareup.kotlinpoet.metadata.isInner
import com.squareup.kotlinpoet.metadata.isInternal
import com.squareup.kotlinpoet.metadata.isLocal
import com.squareup.kotlinpoet.metadata.isPublic
import com.squareup.kotlinpoet.metadata.isSealed
import com.squareup.kotlinpoet.tag
import com.squareup.moshi.Json
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.kotlin.codegen.api.DelegateKey
import com.squareup.moshi.kotlin.codegen.api.PropertyGenerator
import com.squareup.moshi.kotlin.codegen.api.TargetConstructor
import com.squareup.moshi.kotlin.codegen.api.TargetParameter
import com.squareup.moshi.kotlin.codegen.api.TargetProperty
import com.squareup.moshi.kotlin.codegen.api.TargetType
import com.squareup.moshi.kotlin.codegen.api.rawType
import com.squareup.moshi.kotlin.codegen.api.unwrapTypeAlias
import com.squareup.moshi.kotlin.codegen.ksp.isSettable
import kotlinx.metadata.KmConstructor
import kotlinx.metadata.jvm.signature
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.annotation.processing.Messager
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic.Kind.ERROR
import javax.tools.Diagnostic.Kind.WARNING

private val JSON_QUALIFIER = JsonQualifier::class.java
private val JSON = Json::class.asClassName()
private val TRANSIENT = Transient::class.asClassName()
private val VISIBILITY_MODIFIERS = setOf(
  KModifier.INTERNAL,
  KModifier.PRIVATE,
  KModifier.PROTECTED,
  KModifier.PUBLIC
)

private fun Collection<KModifier>.visibility(): KModifier {
  return find { it in VISIBILITY_MODIFIERS } ?: KModifier.PUBLIC
}

@KotlinPoetMetadataPreview
internal fun primaryConstructor(
  targetElement: TypeElement,
  kotlinApi: TypeSpec,
  elements: Elements,
  messager: Messager
): TargetConstructor? {
  val primaryConstructor = kotlinApi.primaryConstructor ?: return null

  val parameters = LinkedHashMap<String, TargetParameter>()
  for ((index, parameter) in primaryConstructor.parameters.withIndex()) {
    val name = parameter.name
    parameters[name] = TargetParameter(
      name = name,
      index = index,
      type = parameter.type,
      hasDefault = parameter.defaultValue != null,
      qualifiers = parameter.annotations.qualifiers(messager, elements),
      jsonName = parameter.annotations.jsonName(),
      jsonIgnore = parameter.annotations.jsonIgnore(),
      serialize = parameter.annotations.serialize(),
      deserialize = parameter.annotations.deserialize(),
    )
  }

  val kmConstructorSignature = primaryConstructor.tag<KmConstructor>()?.signature?.toString()
    ?: run {
      messager.printMessage(
        ERROR,
        "No KmConstructor found for primary constructor.",
        targetElement
      )
      null
    }
  return TargetConstructor(
    parameters,
    primaryConstructor.modifiers.visibility(),
    kmConstructorSignature
  )
}

/** Returns a target type for `element`, or null if it cannot be used with code gen. */
@OptIn(DelicateKotlinPoetApi::class)
@KotlinPoetMetadataPreview
internal fun targetType(
  messager: Messager,
  elements: Elements,
  types: Types,
  element: TypeElement,
  cachedClassInspector: MoshiCachedClassInspector,
): TargetType? {
  val typeMetadata = element.getAnnotation(Metadata::class.java)
  if (typeMetadata == null) {
    messager.printMessage(
      ERROR,
      "@JsonClass can't be applied to $element: must be a Kotlin class",
      element
    )
    return null
  }

  val kmClass = try {
    cachedClassInspector.toKmClass(typeMetadata)
  } catch (e: UnsupportedOperationException) {
    messager.printMessage(
      ERROR,
      "@JsonClass can't be applied to $element: must be a Class type",
      element
    )
    return null
  }

  when {
    kmClass.isEnum -> {
      messager.printMessage(
        ERROR,
        "@JsonClass with 'generateAdapter = \"true\"' can't be applied to $element: code gen for enums is not supported or necessary",
        element
      )
      return null
    }
    !kmClass.isClass -> {
      messager.printMessage(
        ERROR,
        "@JsonClass can't be applied to $element: must be a Kotlin class",
        element
      )
      return null
    }
    kmClass.isInner -> {
      messager.printMessage(
        ERROR,
        "@JsonClass can't be applied to $element: must not be an inner class",
        element
      )
      return null
    }
    kmClass.flags.isSealed -> {
      messager.printMessage(
        ERROR,
        "@JsonClass can't be applied to $element: must not be sealed",
        element
      )
      return null
    }
    kmClass.flags.isAbstract -> {
      messager.printMessage(
        ERROR,
        "@JsonClass can't be applied to $element: must not be abstract",
        element
      )
      return null
    }
    kmClass.flags.isLocal -> {
      messager.printMessage(
        ERROR,
        "@JsonClass can't be applied to $element: must not be local",
        element
      )
      return null
    }
    !kmClass.flags.isPublic && !kmClass.flags.isInternal -> {
      messager.printMessage(
        ERROR,
        "@JsonClass can't be applied to $element: must be internal or public",
        element
      )
      return null
    }
  }

  val kotlinApi = cachedClassInspector.toTypeSpec(kmClass)
  val typeVariables = kotlinApi.typeVariables
  val appliedType = AppliedType(element)

  val constructor = primaryConstructor(element, kotlinApi, elements, messager)
  if (constructor == null) {
    messager.printMessage(
      ERROR,
      "No primary constructor found on $element",
      element
    )
    return null
  }
  if (constructor.visibility != KModifier.INTERNAL && constructor.visibility != KModifier.PUBLIC) {
    messager.printMessage(
      ERROR,
      "@JsonClass can't be applied to $element: " +
        "primary constructor is not internal or public",
      element
    )
    return null
  }

  val properties = mutableMapOf<String, TargetProperty>()

  val resolvedTypes = mutableListOf<ResolvedTypeMapping>()
  val superclass = appliedType.superclasses(types)
    .onEach { superclass ->
      if (superclass.element.getAnnotation(Metadata::class.java) == null) {
        messager.printMessage(
          ERROR,
          "@JsonClass can't be applied to $element: supertype $superclass is not a Kotlin type",
          element
        )
        return null
      }
    }
    .associateWithTo(LinkedHashMap()) { superclass ->
      // Load the kotlin API cache into memory eagerly so we can reuse the parsed APIs
      val api = if (superclass.element == element) {
        // We've already parsed this api above, reuse it
        kotlinApi
      } else {
        cachedClassInspector.toTypeSpec(superclass.element)
      }

      val apiSuperClass = api.superclass
      if (apiSuperClass is ParameterizedTypeName) {
        //
        // This extends a typed generic superclass. We want to construct a mapping of the
        // superclass typevar names to their materialized types here.
        //
        // class Foo extends Bar<String>
        // class Bar<T>
        //
        // We will store {Foo : {T : [String]}}.
        //
        // Then when we look at Bar<T> later, we'll look up to the descendent Foo and extract its
        // materialized type from there.
        //
        val superSuperClass = superclass.element.superclass as DeclaredType

        // Convert to an element and back to wipe the typed generics off of this
        val untyped = superSuperClass.asElement().asType().asTypeName() as ParameterizedTypeName
        resolvedTypes += ResolvedTypeMapping(
          target = untyped.rawType,
          args = untyped.typeArguments.asSequence()
            .cast<TypeVariableName>()
            .map(TypeVariableName::name)
            .zip(apiSuperClass.typeArguments.asSequence())
            .associate { it }
        )
      }

      return@associateWithTo api
    }

  for ((localAppliedType, supertypeApi) in superclass.entries) {
    val appliedClassName = localAppliedType.element.asClassName()
    val supertypeProperties = declaredProperties(
      constructor = constructor,
      kotlinApi = supertypeApi,
      allowedTypeVars = typeVariables.toSet(),
      currentClass = appliedClassName,
      resolvedTypes = resolvedTypes
    )
    for ((name, property) in supertypeProperties) {
      properties.putIfAbsent(name, property)
    }
  }
  val visibility = kotlinApi.modifiers.visibility()
  // If any class in the enclosing class hierarchy is internal, they must all have internal
  // generated adapters.
  val resolvedVisibility = if (visibility == KModifier.INTERNAL) {
    // Our nested type is already internal, no need to search
    visibility
  } else {
    // Implicitly public, so now look up the hierarchy
    val forceInternal = generateSequence<Element>(element) { it.enclosingElement }
      .filterIsInstance<TypeElement>()
      .map { cachedClassInspector.toKmClass(it.metadata) }
      .any { it.flags.isInternal }
    if (forceInternal) KModifier.INTERNAL else visibility
  }

  return TargetType(
    typeName = element.asType().asTypeName(),
    constructor = constructor,
    properties = properties,
    typeVariables = typeVariables,
    isDataClass = KModifier.DATA in kotlinApi.modifiers,
    visibility = resolvedVisibility,
  )
}

/**
 * Represents a resolved raw class to type arguments where [args] are a map of the parent type var
 * name to its resolved [TypeName].
 */
private data class ResolvedTypeMapping(val target: ClassName, val args: Map<String, TypeName>)

private fun resolveTypeArgs(
  targetClass: ClassName,
  propertyType: TypeName,
  resolvedTypes: List<ResolvedTypeMapping>,
  allowedTypeVars: Set<TypeVariableName>,
  entryStartIndex: Int = resolvedTypes.indexOfLast { it.target == targetClass }
): TypeName {
  val unwrappedType = propertyType.unwrapTypeAlias()

  if (unwrappedType !is TypeVariableName) {
    return unwrappedType
  } else if (entryStartIndex == -1) {
    return unwrappedType
  }

  val targetMappingIndex = resolvedTypes[entryStartIndex]
  val targetMappings = targetMappingIndex.args

  // Try to resolve the real type of this property based on mapped generics in the subclass.
  // We need to us a non-nullable version for mapping since we're just mapping based on raw java
  // type vars, but then can re-copy nullability back if it is found.
  val resolvedType = targetMappings[unwrappedType.name]
    ?.copy(nullable = unwrappedType.isNullable)
    ?: unwrappedType

  return when {
    resolvedType !is TypeVariableName -> resolvedType
    entryStartIndex != 0 -> {
      // We need to go deeper
      resolveTypeArgs(targetClass, resolvedType, resolvedTypes, allowedTypeVars, entryStartIndex - 1)
    }
    resolvedType.copy(nullable = false) in allowedTypeVars -> {
      // This is a generic type in the top-level declared class. This is fine to leave in because
      // this will be handled by the `Type` array passed in at runtime.
      resolvedType
    }
    else -> error("Could not find $resolvedType in $resolvedTypes. Also not present in allowable top-level type vars $allowedTypeVars")
  }
}

/** Returns the properties declared by `typeElement`. */
@KotlinPoetMetadataPreview
private fun declaredProperties(
  constructor: TargetConstructor,
  kotlinApi: TypeSpec,
  allowedTypeVars: Set<TypeVariableName>,
  currentClass: ClassName,
  resolvedTypes: List<ResolvedTypeMapping>
): Map<String, TargetProperty> {
  val result = mutableMapOf<String, TargetProperty>()
  for (initialProperty in kotlinApi.propertySpecs) {
    val resolvedType = resolveTypeArgs(
      targetClass = currentClass,
      propertyType = initialProperty.type,
      resolvedTypes = resolvedTypes,
      allowedTypeVars = allowedTypeVars
    )
    val property = initialProperty.toBuilder(type = resolvedType).build()
    val name = property.name
    val parameter = constructor.parameters[name]
    result[name] = TargetProperty(
      propertySpec = property,
      parameter = parameter,
      visibility = property.modifiers.visibility(),
      jsonName = parameter?.jsonName ?: property.annotations.jsonName() ?: name,
      jsonIgnore = parameter?.jsonIgnore == true || property.annotations.jsonIgnore(),
      serialize = parameter?.serialize != false || property.annotations.serialize(),
      deserialize = parameter?.deserialize != false || property.annotations.deserialize(),
    )
  }

  return result
}

internal val TargetProperty.isSettable get() = propertySpec.mutable || parameter != null
private val TargetProperty.isVisible: Boolean
  get() {
    return visibility == KModifier.INTERNAL ||
      visibility == KModifier.PROTECTED ||
      visibility == KModifier.PUBLIC
  }

/**
 * Returns a generator for this property, or null if either there is an error and this property
 * cannot be used with code gen, or if no codegen is necessary for this property.
 */
internal fun TargetProperty.generator(
  messager: Messager,
  sourceElement: TypeElement,
  elements: Elements,
): PropertyGenerator? {
  if (isIgnore()) {
    if (!hasDefault) {
      messager.printMessage(
        ERROR,
        "No default value for ignored property $name",
        sourceElement
      )
      return null
    }
    return PropertyGenerator(this, DelegateKey(type, emptyList()), isSettable)
  }

  if (!isVisible) {
    messager.printMessage(
      ERROR,
      "property $name is not visible",
      sourceElement
    )
    return null
  }


  // Merge parameter and property annotations
  val qualifiers = parameter?.qualifiers.orEmpty() + propertySpec.annotations.qualifiers(messager, elements)
  for (jsonQualifier in qualifiers) {
    val qualifierRawType = jsonQualifier.typeName.rawType()
    // Check Java types since that covers both Java and Kotlin annotations.
    val annotationElement = elements.getTypeElement(qualifierRawType.canonicalName)
      ?: continue

    annotationElement.getAnnotation(Retention::class.java)?.let {
      if (it.value != RetentionPolicy.RUNTIME) {
        messager.printMessage(
          ERROR,
          "JsonQualifier @${qualifierRawType.simpleName} must have RUNTIME retention"
        )
      }
    }
  }

  val jsonQualifierSpecs = qualifiers.map {
    it.toBuilder()
      .useSiteTarget(AnnotationSpec.UseSiteTarget.FIELD)
      .build()
  }

  return PropertyGenerator(
    this,
    DelegateKey(type, jsonQualifierSpecs),
    isSettable
  )
}

private fun List<AnnotationSpec>?.qualifiers(
  messager: Messager,
  elements: Elements
): Set<AnnotationSpec> {
  if (this == null) return setOf()
  return filterTo(mutableSetOf()) {
    val typeElement: TypeElement? = elements.getTypeElement(it.typeName.rawType().canonicalName)
    if (typeElement == null) {
      messager.printMessage(WARNING, "Could not get the TypeElement of $it")
    }
    typeElement?.getAnnotation(JSON_QUALIFIER) != null
  }
}

private fun List<AnnotationSpec>?.jsonName(): String? {
  if (this == null) return null
  return filter { it.typeName == JSON }.firstNotNullOfOrNull { annotation ->
    annotation.jsonName()
  }
}

private fun List<AnnotationSpec>?.jsonIgnore(): Boolean {
  if (this == null) return false
  return filter { it.typeName == JSON }.firstNotNullOfOrNull { annotation ->
    annotation.jsonIgnore()
  } ?: false
}

//yizems
private fun List<AnnotationSpec>?.serialize(): Boolean {
  if (this == null) return false
  return filter { it.typeName == JSON }.firstNotNullOfOrNull { annotation ->
    annotation.serialize()
  } ?: true
}

private fun List<AnnotationSpec>?.deserialize(): Boolean {
  if (this == null) return false
  return filter { it.typeName == JSON }.firstNotNullOfOrNull { annotation ->
    annotation.deserialize()
  } ?: true
}

private fun AnnotationSpec.jsonName(): String? {
  return elementValue<String>("name").takeUnless { it == Json.UNSET_NAME }
}

private fun AnnotationSpec.jsonIgnore(): Boolean {
  return elementValue<Boolean>("ignore") ?: false
}

//yizems
private fun AnnotationSpec.serialize(): Boolean {
  return elementValue<Boolean>("serialize") ?: true
}

private fun AnnotationSpec.deserialize(): Boolean {
  return elementValue<Boolean>("deserialize") ?: true
}

private fun <T> AnnotationSpec.elementValue(name: String): T? {
  val mirror = requireNotNull(tag<AnnotationMirror>()) {
    "Could not get the annotation mirror from the annotation spec"
  }
  @Suppress("UNCHECKED_CAST")
  return mirror.elementValues.entries.firstOrNull {
    it.key.simpleName.contentEquals(name)
  }?.value?.value as? T
}

internal val TypeElement.metadata: Metadata
  get() {
    return getAnnotation(Metadata::class.java)
      ?: throw IllegalStateException("Not a kotlin type! $this")
  }

private fun <E> Sequence<*>.cast(): Sequence<E> {
  return map {
    @Suppress("UNCHECKED_CAST")
    it as E
  }
}
