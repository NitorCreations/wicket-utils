wicket-utils
============

Useful components, models and other stuff for Wicket (mainly 6) projects

## Maven

Available in Maven central repository. Add this to your `pom.xml`

```xml
<dependency>
  <groupId>com.nitorcreations</groupId>
  <artifactId>wicket-components</artifactId>
  <version>1.7</version>
</dependency>
```

## Contents

### Components
- `ViewSizeToolbar` - change the view size of a `DataTable`
- `MaxLengthTextField` 
- `ShorteningLabel` - shortens the display value and adds an ellipsis
- `CollapsingBorder` and `CollapsingContainer` for ajax based collapsing content

### Form components
- `AjaxElementChoice` - works similarly to a radio group but on arbitrary components

### Converter
- Converters for Joda-Time's `LocalDate`, `LocalTime`, and `DateTime`

### Models
- `GroupingMultimapModel` / `GroupingModel` - A model that groups a list of items by a Lambdaj argument specified by getKeyArgument(). The lists will be kept in order by specifying Ordering for keys and values in getKeyOrdering() and getValueOrdering(), correspondingly.
- `JoiningPropertyModel` - joins a list by using the property expression and ordering them naturally. E.g., `List&lt;String> { "abc", "d", "ef" }` could be joined by `length` to "1, 2, 3"
- `ListingStringResourceModel` - joins a list by internally wrapping `StringResourceModels`. E.g., properties: `stringLength=len: ${length}`, Java: `new ListingStringResourceModel&lt;String>("stringLength", Model.ofList(Arrays.asList("abc", "d", "ef")))` => "len: 3, len: 1, len: 2"
- `MapEntryModel` - a dynamic map entry accessor model
- `NullWrapperModel(IModel first, IModel second)` - returns `second.getObject()` if `first.getObject()` is `null`
- `StringFormatModel` - works like `String.format(..)`

### Behaviors
- `VisibilityNullBehavior` Factories for `Behavior`s which sets the `Component`'s visibility based on the default model's or some other components model object. Used to reduce boiler-plate code with typical scenarios of components needing to be hidden or visible based on nullity of certain model object.
- `ErrorFieldBehavior` - Adds red borders to any components with validation errors (Since 1.1)

### Protocol.http
- `WicketAjaxTimeoutFilter` - refreshes the page on ajax request after session has expired. Works with Wicket 6.

### Util
- `AjaxUtil` - helpers for common Wicket ajax tasks: `add(target, Component...)`, `add(Component...)`, `getTarget()`

### Events
- `AnnotationEventDispatcher` - scans instantiated components for `OnEvent` annotated methods and dispatches events based on payload type.

### Validators
- `CheckedValidator` - validates that a checkbox is checked (Since 1.1)
- `MatchingValidator` - validates that the value matches a given Hamcrest matcher (Since 1.1)
- `PhoneNumberValidator` - validates that the String value contains only `0-9+-() ` (Since 1.1)
