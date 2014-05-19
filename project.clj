(defproject my-personal-kanban-local-cloud "0.1.0-SNAPSHOT"
  :description "My Personal Kanban Local Cloud"
  :url "http://to-do.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-core "1.2.2"]
                 [ring/ring-jetty-adapter "1.2.2"]
                 [org.clojure/data.json "0.2.4"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler mpk.core/mpk-app}
  :main mpk.core
  :prep-tasks[["compile" "mpk.action-handlers"]
              "javac" "compile"])

