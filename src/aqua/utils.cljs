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
      (cond
        (or
         (and (= :initial from) (= :no-stop to))
         (and (= :initial from) (<= d to))
         (and (>= d from) (= :no-stop to))
         (and (>= d from) (<= d to))) (animate d m)
        (> d to) (animate to m)
        (< d from) (animate from m)))))

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
  [^js obj ids]
  (into {}
        (map (fn [id]
               {id (.getElementById obj id)})
             ids)))

(defn collect-from-classes
  [^js obj classes]
  (into {}
        (map (fn [class]
               {class (array-seq
                       (.getElementsByClassName obj class))})
             classes)))

(defn collect-nodes
  [m ^js obj]
  {:ids (collect-from-ids obj (:ids m))
   :classes (collect-from-classes obj (:classes m))})

(defn subscribe-effect
  [{:keys [container-id inline external animations initiate debug?]}]
  (let [container-node {:container-node (js/document.getElementById container-id)}
        animations {:animations animations}
        initiate {:initiate initiate}]
    (if external
      (let [^js object-node (js/document.getElementById (:object-id external))]
        ;; wait for all resources to load
        (.addEventListener
         js/window
         "load"
         #(let [content (.-contentDocument object-node)]
           (register-effect (merge
                             container-node
                             animations
                             initiate
                             {:object-node object-node}
                             {:inline (collect-nodes inline js/document)}
                             {:external (collect-nodes external ^js content)})
                            debug?))))
      (register-effect (merge
                        container-node
                        animations
                        initiate
                        {:inline (collect-nodes inline js/document)})
                       debug?))))
