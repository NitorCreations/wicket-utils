# 1.5
- Added possibility to `SetConverter` and `ListConverter` to supply the internal converter as a constructor parameter instead of using a global setting from the application

# 1.4
- Added `SetConverter` similar to `ListConverter`
- Added `SetToListWrapperModel` to facilitate using sets as the backing collection for, e.g., list views 
- `ListConverter` and `SetConverter` now both convert empty string to empty collection instead of failing

# 1.3
- Added `ListConverter` to convert strings to lists of objects and back, e.g., with TextFields

# 1.2
- Added `ChoiceModel` to allow choosing between two different models with a boolean flag
- Added `CollapsingBorder` - a border with togglable visibility for inner content
- Fixes to previous functionality

# 1.1
- Added `AnnotationEventDispatcher`
- Added `ErrorFieldBehavior`
- Added validators
