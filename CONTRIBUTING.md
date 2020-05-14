# Contributing to Blocks

## How can I contribute?

### Reporting bugs
You can create a ticket for it [here](https://github.com/rvandoosselaer/Blocks/issues).
When you are creating a bug report, please include as many details as possible. The steps to reproduce, version of 
Blocks, your environment, an error stacktrace, log output, ...

> Note: If you find a Closed issue that seems like it is the same thing that you're experiencing, open a new issue and 
> include a link to the original issue in the body of your new one.

### Requesting a new feature
You can create a ticket for it [here](https://github.com/rvandoosselaer/Blocks/issues).
When you are creating an enhancement suggestion, please include as many details as possible.

Use a clear and descriptive title for the issue to identify the suggestion.
Provide a step-by-step description of the suggested enhancement in as many details as possible. Describe the current 
behavior and explain which behavior you expected to see instead and why.

## Code guidelines

### Indent style
We use the "one true brace style" ([1TBS](https://en.wikipedia.org/wiki/Indentation_style#Variant:_1TBS_(OTBS))). 
Indent size is 4 columns.

```java
if (x > 0) {
    positive(x);
} else {
    negative(x);
}
```

### Documentation
Try to document (public) methods that perform logic that might not be clear at first sight. A method doc should tell what 
the method does. Depending on the argument types, it may also be important to document input format.

### Optional
Public methods that can return `null` should wrap the returning object in an Optional.

### Strings
To separate words in a string, use a hypen or dash (-). An underscore is considered part of a word.