Aqua logo

[![Clojars Project](https://img.shields.io/clojars/v/aqua.svg)](https://clojars.org/aqua)

## What is Aqua?

It is a Clojurescript library that allows you to seamlessly animate DOM elements *on-scroll*.

## Why should you use it?

- Granular - *From trivial to highly complex DOM nodes manipulations*
- Universal - *Target both inline elements and external SVGs objects*
- Extra Light - *Only a handful of internal functions and one API entry point*
- No dependencies - *Yes, only interop*

As you must be wondering "What is so special about it?", I will go straight to the code implementation. Let's start!

## API

### Require Aqua

#### In Deps

```clojure
aqua {:mvn/version "0.1.0"}
```

or

```clojure
aqua {:git/url "https://github.com/luciodale/aqua.git"
:sha "copy and paste last commit sha"}
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
