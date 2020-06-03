<div style="display: inline;">
<img src="https://s5.gifyu.com/images/ezgif.com-video-to-gifa7e25dd3820dfd28.gif" width="250" />
<img src="https://s5.gifyu.com/images/ezgif.com-video-to-gif-163474dc641dcf465.gif" width="250" />
<img src="https://s5.gifyu.com/images/ezgif.com-video-to-gif-28409f37786f09158.gif" width="196"/>
</div>

See it live at https://www.opencrux.com/

[![Clojars Project](https://img.shields.io/clojars/v/aqua.svg)](https://clojars.org/aqua)

## What is Aqua?

Aqua is a Clojurescript library that allows you to seamlessly animate DOM elements *on-scroll*.

## Why should you use it?

- Granular span - *From trivial to complex animations*
- Universal - *For inline elements and external SVGs objects*
- Extra Light - *Only a handful of internal functions and one API entry point*
- No dependencies - *Yes, only interop*
- Web & Mobile - *The code is simple enough to work on all platforms*

As you must be excited about rolling your own awesome animations, I am going to show you how easy it is to get started. Here we go!

## API

### Require Aqua

#### In Deps

```clojure
aqua {:mvn/version "0.2.2"}
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
   {:container "#element"
    :inline {:ids ["element"]}
    :animations
    [{:from 0
      :to :no-stop
      :animate
      (fn [[offset _] m]
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

#### Output
<img src="https://s5.gifyu.com/images/ezgif.com-gif-makeraf56e657628d6f15.gif" width="200" />

What follows is an explanation of all the available map keys:
- `:container` - *The reference to the element of your animation, being a class or an id*

- `:inline` - *To specify that the elements we are targeting are expressively defined in the HTML code*
- `:ids` - *A sequence of all ids whose elements are needed for the animation*
- `:classes` - *A sequence of all classes whose elements are needed for the animation*

- `:external` - *To indicate that we are using for example and svg sourced via the `object` HTML tag*
- `:object-id` - *The id of the object element that imports the svg*
- `:ids` - *A sequence of all SVG elements ids needed for the animation*
- `:classes` - *A sequence of all SVG elements classes needed for the animation*

- `:initiate` - *A function that is run only once to allow any pre-effect element manipulation*

- `:animations` - *A sequence of animations that are attached to the on-scroll event to perform all side effects*
- `:from` - *To specify when the event should start* - in px and based on `:container`
- `:to` - *To specify when the event should stop* - in px and based on `:container`
- `:animate` - *A function that provides the offset and all resolved DOM elements*

### More words on the functionality

The container element whose id or class is passed to the `:container` key, directly affects the offset value provided in the `:animate` anonymous function. In brief, as the element enteres the viewport from the bottom of the screen, the offset will have positive values such as 10, 100, 1000, and so on. Clearly, when the element is hidden further down the page, the offset will have negative values. In addition to the offset pixel value, a percentage value can be destructured from the same vector at index 1. It indicates the animation progress from 0 to 1.

The `:inline` and `:external` keys allow the gathering of all DOM elements needed to be animated. In practice, the id and class names provided will be replaced with the elements whose style properties can be updated. Keep in mind that to apply some changes to all the elements of one class, a loop is needed as per example.

```clojure
(aqua/subscribe
   {:container "#container"
    :inline {:classes ["element"]}
    :animations
    [{:from 0
      :to :no-stop
      :animate
      (fn [[offset _] m]
        (let [elements (-> m :inline :classes (get "element"))
              offset (/ offset 3)]
          (doseq [element elements]
            (do
              (set! (-> element .-style .-background)
                    (str "rgb("
                         (mod offset 255) ","
                         (mod offset 255) ","
                         (mod offset 255)")"))
              (set! (-> element .-style .-transform)
                    (str "rotate(-" (/ offset 20 js/Math.PI) "rad)"))))))}]})

[:div#container
      {:style {:height "auto"
               :display "flex"
               :flex-wrap "wrap"}}
      (for [x (range 27)]
        ^{:key x}
        [:div.element
         {:style {:width "50px"
                  :height "50px"
                  :margin "1em"
                  :background "green"}}])]
```

#### Output
<img src="https://s5.gifyu.com/images/ezgif.com-video-to-gif-3b3cd334bf607b9a1.gif" width="200" />

An additional example with an external SVG file is provided below.

```clojure
(aqua/subscribe
   {:container "#element"
    :external {:object-id "element"
               :ids ["pin-path" "oval"]}
    :animations
    [{:from 0
      :to :no-stop
      :animate
      (fn [[offset _] m]
        (let [pin (-> m :external :ids (get "pin-path"))
              oval (-> m :external :ids (get "oval"))]
          (doseq [el [pin oval]]
            (set! (-> el .-style .-transform)
                  (str "rotate(-" (/ offset 20 js/Math.PI) "rad)")))))}]})

[:object
      {:id "element"
       :data "/svg"}]
```

#### Output
As you can notice, you can target single elements within the SVG code

<img src="https://s5.gifyu.com/images/ezgif.com-video-to-gif-4c6c5d1bddf1390bc.gif" width="200" />

Moving to the animations, the `:from` and `:to` keys can have any numeric value plus two helpers being `:no-stop` for the `:to` key and `:initial` for the `:from` key. They indicate that the effect is not restricted to hard coded values.

The `:initiate` key is particularly useful when you want to draw a path on scroll, as the `strokeDasharray` and `strokeDashoffset` need to be properly set. If you don't know in advanced what the length of the path is, you can dynamically set the element style like the following example:

```clojure
(aqua/subscribe
   {:container "#element"
    :external {:object-id "element"
               :ids ["path"]}
    :initiate
    (fn [offset m]
      (let [path (-> m :external :ids (get "path"))]
        (set! (-> path .-style .-strokeDasharray) (.getTotalLength path))
        (set! (-> path .-style .-strokeDashoffset) (.getTotalLength path))))
    :animations
    [{:from 100
      :to 500
      :animate
      (fn [[_ progress] m]
        (let [path (-> m :external :ids (get "path"))
              length (.getTotalLength path)]
           (set! (-> path .-style .-strokeDashoffset) (- (.getTotalLength path) (* length progress)))))}]})
```

#### Output
<img src="https://s5.gifyu.com/images/ezgif.com-video-to-gif-55d3025bff1dd20e5.gif" width="200">

## Single Page Application (SPA) and React

From version 0.1.5, Aqua is fully compatible with any Javascript framework and can be triggered to animate any mounted and unmounted element in a SPA context.

## Debug

Several useful debugging messages and warnings have been added and can be turned on by including a `:debug? true` key value pair in each map.

```clojure
(aqua/subscribe
   {...
    :debug? true
    ...})
```

## Final notes

When targeting the inline DOM elements with the `:inline` keyword, make sure that the script you will use to include Aqua is placed at the bottom of the HTML to allow all elements to be rendered first. If you are including some external SVG with the `object` HTML tag, use a `load` listener on the `js/window` object to make sure all resources are rendered AND loaded.

When using Aqua from React, you could attach an `:onLoad` listener to an `object` tag as follows:

```clojure
[:object#your-id {:data "images/your-illustration.svg"
                  :onLoad subscribe-animation}]
```

Where `subscribe-animation` is a wrapper of the `subscribe` function. Also, the lifecyle method `componentDidMount` might be used as well for inline DOM elements. However, if external resources are targeted, some extra logic is needed, as the above mentioned method is invoked when the component is rendered but not necessarily loaded.
