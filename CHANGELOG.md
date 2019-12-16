# Changelog
All notable changes to this project will be documented in this file.

## [0.1.5]
### Added
- Single Page Application and React support

### Changed
- Remove javascript listeners that fire only once i.e. `js/window.load`

#### Comment
The developer has to make sure that the page has loaded the DOM elements to be targeted in the effects, before the `subscribe` function is invoked.

## [0.2.0]
### Added
- Percent variable param in effect function

### Comment
The developer has to destructure the offset value from a vector i.e. `(fn [[offset progress] nodes] ...)`

## [0.2.1]
### Added
- Option to target id or class as container element

### Comment
When using a class, only the first one found in the document will be used, as the underlying code implements the querySelector function.
