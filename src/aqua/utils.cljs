(ns aqua.utils)

(defn optimize-scroll
  [type name]
  (let [running (atom nil)
        func (fn []
               (when-not @running
                 (reset! running true)
                 (js/requestAnimationFrame
                  (fn [_]
                    (.dispatchEvent
                     js/window
                     (js/CustomEvent. name))
                    (reset! running false)))))]
    (js/window.addEventListener type func)))

(defonce optimize-scroll-listener
  (optimize-scroll "scroll" "optimizedScroll"))

(defn distance-to-bottom
  [^js div-node]
  (+ (- (.-offsetTop div-node))
     js/window.innerHeight
     js/window.pageYOffset))

(defn debug-print [m]
  (js/console.log m))

(defn run-effect!
  [{:keys [container-node animations] :as m}]
  (let [d (distance-to-bottom container-node)]
    (doseq [{:keys [from to animate]} animations]
      (try
        (cond
          (or
           (and (= :initial from) (= :no-stop to))
           (and (= :initial from) (<= d to))
           (and (>= d from) (= :no-stop to))
           (and (>= d from) (<= d to))) (animate d m)
          (> d to) (animate to m)
          (< d from) (animate from m))
        (catch js/Error e
          (js/console.err
           (str "Exception thrown in one of the animation functions" e)))))))

(defn register-effect
  [m debug?]
  (let [d (distance-to-bottom (:container-node m))]
    (when debug? (debug-print m))
    (when-let [init-f (:initiate m)]
      (init-f d m))
    (run-effect! m)
    (js/window.addEventListener
     "optimizedScroll"
     #(run-effect! m))))

(defn collect-from-ids
  [^js obj ids debug?]
  (into {}
        (map (fn [id]
               (let [node (.getElementById obj id)]
                 (when (and (nil? node) debug?)
                   (js/console.warn
                    (str "The id " id
                         " could not be found or its element has not been loaded to the DOM."
                         " Make sure the element is rendered AND loaded before the subscribe function is invoked.")))

                 {id (.getElementById obj id)}))
             ids)))

(defn collect-from-classes
  [^js obj classes debug?]
  (into {}
        (map (fn [class]
               (let [nodes (array-seq (.getElementsByClassName obj class))]
                 (when (and (= 0 (count nodes)) debug?)
                   (js/console.warn
                    (str "The class " class
                         " could not be found or its elements have not been loaded to the DOM."
                         " Make sure the class elements are rendered AND loaded before the subscribe function is invoked.")))
                 {class nodes}))
             classes)))

(defn collect-nodes
  [m ^js obj debug?]
  {:ids (collect-from-ids obj (:ids m) debug?)
   :classes (collect-from-classes obj (:classes m) debug?)})

(defn subscribe-effect
  [{:keys [container-id inline external animations initiate debug?]}]
  (let [container-node {:container-node (js/document.getElementById container-id)}
        animations {:animations animations}
        initiate {:initiate initiate}]
    (register-effect
     (merge
      container-node
      animations
      initiate
      {:inline (collect-nodes inline js/document debug?)}
      (when-let [^js object-node (js/document.getElementById (:object-id external))]

        {:object-node object-node
         :external (collect-nodes external (.-contentDocument object-node) debug?)}))
     debug?)))
