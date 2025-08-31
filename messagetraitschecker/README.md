# How this module works?

This is a module that enforces certain constrains on subclasses
of a class annotated with ``MessageTraits`` annotation.

## MessageTraits annotation

If a class is annotated with ``MessageTraits``  annotation
(directly or indirectly) and is not abstract then:

- It should have a ``public static final byte id`` field with a default value.
- It should be final

``id`` values should be unique among different classes annotated
with ``MessageTraits``.

## AutoMessageTraits annotation

If a class is annotated with ``AutoMessageTraits`` indirectly then:

- It should be abstract
- It should have one template argument which is a ``record``

Template parameters given to class annotated with `AutoMessageTraits` should be
unique among subclasses.

A concrete subclass will be generated and added to correct maps stored in
`GeneratedClassesData` which will also be a generated class
