# Collaborative text editor

For document modification I use abstract class *CommandBase*. The class has two children: *InsertCommand* and *DeleteCommand* - which override the method *apply*.

In the interface *CommandTransformation* there is a method *transformation* which transforms input command. There are four implementations of this interface: *InsertAfterInsertTransformation, InsertAfterDeleteTransformation, DeleteAfterInsertTransformation, DeleteAfterDeleteTransformation*.

The interface *TransformationFactory* has a method for selection of a certain *CommandTransformation*. Class *InclusionTransformationFactory* implements this interface. It injects a list of *CommandTransformation* and implements the logic for selection of a certain implementation of *CommandTransformation*.

Each document has a list of applied commands and injects the interfaces *TransformationFactory* and *NotificationService*. The interface *NotificationService* is responsible for notifing UI about changes in document.

My suggestion about RESTful API for work with documents can be found by using this [link](https://app.swaggerhub.com/apis-docs/mavrinkirill/collaborative-text-editor/1.0.0).
