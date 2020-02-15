# Collaborative text editor

For document modification I use abstract class *CommandBase*. The class has two children: *InsertCommand* and *DeleteCommand* - which override the method *apply()*.

Each document has a list of applied commands and injects the interface *TransformationService*. This interface has a method for rule selection and command transformation if it is necessary.
Interface *CommandTransformation* has methods which describe every certain rule for transformation.
Also there is a *DocumentService* which encapsulates logic of working with documents, has mappings and persists documents in memory.

My suggestion about RESTful API for work with documents can be found by using this [link](https://app.swaggerhub.com/apis-docs/mavrinkirill/collaborative-text-editor/1.0.0).
