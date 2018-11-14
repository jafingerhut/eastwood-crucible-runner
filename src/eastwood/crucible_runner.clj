(ns eastwood.crucible-runner
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :as cli]
            [clojure.string :as str]))


(def cli-options
  [["-v" "--versions FILENAME" "Data file describing combination of (Eastwood, Clojure, JDK) versions to run"]
   ["-H" "--test-help" "Display this help message"]])


(defn- help [args]
  (println "\nUsage:\n")
  (println "clj -m" (namespace `help) "<options>\n")
  (println (:summary args)))


(defn -main
  "Entry point for the Eastwood crucible test runner"
  [& args]
  (let [args (cli/parse-opts args cli-options)]
    (if (:errors args)
      (do (doseq [e (:errors args)]
            (println e))
          (help args))
      (if (-> args :options :test-help)
        (help args)
        (let [opts (:options args)]
          (println (format "opts=%s" opts)))))))
