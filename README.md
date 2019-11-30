Aqua logo

[![Clojars Project](https://img.shields.io/clojars/v/aqua.svg)](https://clojars.org/aqua)

## What is Aqua?

Aqua is a Clojurescript library that allows you to seamlessly animate DOM elements *on-scroll*.

## Why should you use it?

- Granular degree - *From trivial to complex animations*
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

The registration of animations is very straight forward. Call the `subscribe` function, and pass any number of maps with your *on-scroll* effects data.

```clojure
(aqua/subscribe
   {:container-id "element"
    :inline {:ids ["element"]}
    :animations
    [{:from 0
      :to :no-stop
      :animate
      (fn [offset m]
        (let [element (-> m :inline :ids (get "element"))]
          (set! (-> element .-style .-transform)
                (str "rotate(-" (/ offset 100 js/Math.PI) "rad)"))))}]})
```
                
Here, we assume that we have a div element, styled to look like a square, that rotates as we scroll. The HTML code might look like this:

```clojure
[:div#element
      {:style {:width "200px"
               :height "200px"
               :background "green"}}]
```

#### Output: 
<img src="https://s5.gifyu.com/images/ezgif.com-gif-makeraf56e657628d6f15.gif" width="200" />

What follows is an explanation of all the available map keys:
- `:container-id` - *The reference element of your animation*

- `:inline` - *To specify that the elements we are targeting are expressively defined in the HTML code*
- `:ids` - *A sequence of all ids whose elements are needed for the animation*
- `:classes` - *A sequence of all classes whose elements are needed for the animation*

- `:external` - *To indicate that we are using for example and svg sourced via the `object` HTML tag*
- `:object-id` - *The id of the object element that imports the svg*
- `:ids` - *A sequence of all SVG elements ids needed for the animation*
- `:classes` - *A sequence of all SVG elements classes needed for the animation*

- `:initiate` - *A function that is run only once to allow any pre-effect element manipulation*

- `:animations` - *A sequence of animations that are attached to the on-scroll event to perform all side effects*
- `:from` - *To specify when the event should start* - in px and based on `:container-id`
- `:to` - *To specify when the event should stop* - in px and based on `:container-id`
- `:animate` - *A function that provides the offset and all resolved DOM elements*


