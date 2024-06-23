(defproject hangman-cljs "0.1.0"
  :description "A P2P hangman game written using ClojursScript, Reagent and WebRTC"
  :url "https://github.com/mjholub/hangman-cljs"
  :license {:name "AGPL-3.0"
            :url "https://www.gnu.org/licenses/agpl-3.0.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [reagent "1.2.0"]]
  :plugins [[lein-figwheel "0.5.20"]
            [lein-cljsbuild "1.1.8"]]
  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :figwheel {:on-jsload "hangman-cljs.core/mount-root"}
                             :compiler {:main "hangman-cljs.core"
                                        :output-to "resources/public/js/app.js"
                                        :output-dir "resources/public/js/out"
                                        :asset-path "js/out"
                                        :optimizations :all
                                        :source-map-timestamp true
                                        :source-map true}}}}
  :main ^:skip-aot hangman-cljs.core
  :target-path "target/%s"
  :profiles
  {:dev {:dependencies [[binaryage/devtools "1.0.7"]
                        [figwheel-sidecar "0.5.20"]
                        [cider/piggieback "0.5.3"]
                        [weasel "0.7.1"]]
         :plugins [[lein-figwheel "0.5.20"]]
         :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
         :figwheel {:on-jsload "hangman-cljs.core/mount-root"}}})
