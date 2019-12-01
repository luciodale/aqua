(defproject aqua "0.1.4"
  :description "Clojurescript on scroll animations for DOM elements, including external SVGs."
  :url "https://github.com/luciodale/aqua"
  :license {:name "MIT"}
  :source-paths ["src"]
  :profiles {:uberjar {:aot :all}}
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]])
