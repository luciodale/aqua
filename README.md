Aqua logo

[![Clojars Project](https://img.shields.io/clojars/v/aqua.svg)](https://clojars.org/aqua)

## What is Aqua?

It is a Clojurescript library that allows you to seamlessly animate DOM elements *on-scroll*.

## Why should you use it?

- Granular degree - *From trivial to highly complex DOM nodes manipulations*
- Universal - *For inline elements and external SVGs objects*
- Extra Light - *Only a handful of internal functions and one API entry point*
- No dependencies - *Yes, only interop*

As you must be excited about rolling your own awesome animations, I am going to show you how easy it is to get started. Here we go!

## API

### Require Aqua

#### In Deps

```clojure
aqua {:mvn/version "0.1.1"}
```

or

```clojure
aqua {:git/url "https://github.com/luciodale/aqua.git"
:sha "last commit"}
 ```

#### In Namespace

```clojure
(ns your.namespace
  (:require
  [aqua.core :as aqua]))
```

### Subscribe your animations

The registration of animations is very straight forward. Call the `subscribe` function, and pass a map or a sequence of maps of your *on-scroll* effects.

- show example of dom elment [:div ...]
