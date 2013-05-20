wicket-utils
============

Useful components, models and other stuff for Wicket (mainly 6) projects

### Components
- `ViewSizeToolbar` - change the view size of a `DataTable`
- `MaxLengthTextField` 
- `ShorteningLabel` - shortens the display value and adds an ellipsis

### Converter
- Converters for Joda-Time's `LocalDate`, `LocalTime`, and `DateTime`

### Models
- `GroupingMultimapModel` / `GroupingModel` - A model that groups a list of items by a Lambdaj argument specified by getKeyArgument(). The lists will be kept in order by specifying Ordering for keys and values in getKeyOrdering() and getValueOrdering(), correspondingly.
- `JoiningPropertyModel` - joins a list by using the property expression and ordering them naturally. E.g., `List&lt;String> { "abc", "d", "ef" }` could be joined by `length` to "1, 2, 3"
- `ListingStringResourceModel` - joins a list by internally wrapping `StringResourceModels`. E.g., properties: `stringLength=len: ${length}`, Java: `new ListingStringResourceModel&lt;String>("stringLength", Model.ofList(Arrays.asList("abc", "d", "ef")))` => "len: 3, len: 1, len: 2"
- `MapEntryModel` - a dynamic map entry accessor model
- `NullWrapperModel(IModel first, IModel second)` - returns `second.getObject()` if `first.getObject()` is `null`
- `StringFormatModel` - works like `String.format(..)`

### Protocol.http
- `WicketAjaxTimeoutFilter` - refreshes the page on ajax request after session has expired. Works with Wicket 6.

### Util
- `AjaxUtil` - helpers for common Wicket ajax tasks: `add(target, Component...)`, `add(Component...)`, `getTarget()`
