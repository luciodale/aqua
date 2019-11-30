(ns core-test
  (:require
   [aqua.utils :as utils]
   [cljs.test :refer-macros [deftest is testing]]))

(defn create-elements
  ([ids]
   (doseq [id ids]
     (let [el (js/document.createElement "div")]
       (set! (.-id el) id)
       (js/document.body.appendChild el))))
  ([classes times]
   (doseq [class classes
           one (range times)]
     (let [el (js/document.createElement "div")]
       (set! (.-className el) class)
       (js/document.body.appendChild el)))))

(defn clean-up-dom
  [{:keys [classes ids]}]
  (doseq [class classes]
    (let [nodes (array-seq
                 (js/document.getElementsByClassName class))]
      (while (> (count nodes) 0)
        (doseq [node nodes]
          (-> node .-parentNode (.removeChild node))))))
  (doseq [id ids]
    (let [node (js/document.getElementById id)]
      (-> node .-parentNode (.removeChild node)))))

(defn rand-strs [t]
  (repeatedly t #(str (gensym))))

(deftest collect-from-ids
  (testing "ids->keywords equality and dom elements existance"
    (let [id-count 4
          ids (rand-strs id-count)
          ;; side effect
          _ (create-elements ids)
          nodes (utils/collect-from-ids
                 js/document
                 ids)]
      (is (= ids
             (mapv first nodes)))
      (is (every? some? (map second nodes)))
      (is (= id-count (count nodes)))
      (clean-up-dom {:ids ids}))))

(deftest collect-from-classes
  (testing "classes->keywords equality and dom elements existance"
    (let [class-count 4
          nodes-per-class 4
          classes (rand-strs class-count)
          ;; side effect
          _ (create-elements classes nodes-per-class)
          nodes (utils/collect-from-classes
                 js/document
                 classes)]
      (is (= classes
             (mapv first nodes)))
      (is (->> nodes
               (map second)
               concat
               (every? some?)))
      (is (every? #(= nodes-per-class
                      (count (second %)) nodes)))
      (clean-up-dom {:classes classes}))))
