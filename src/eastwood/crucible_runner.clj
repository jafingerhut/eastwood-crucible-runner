(ns eastwood.crucible-runner
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :as cli]
            [clojure.string :as str]))


(defn- accumulate [m k v]
  (update-in m [k] (fnil conj #{}) v))


(def cli-options
  [
   ["-d" "--dir DIRNAME" "Name of the directory containing tests. Defaults to \"test\"."
    :parse-fn str
    :assoc-fn accumulate]
   ["-n" "--namespace SYMBOL" "Symbol indicating a specific namespace to test."
    :parse-fn symbol
    :assoc-fn accumulate]
   ["-r" "--namespace-regex REGEX" "Regex for namespaces to test. Defaults to #\".*-test$\"\n                               (i.e, only namespaces ending in '-test' are evaluated)"
    :parse-fn re-pattern
    :assoc-fn accumulate]
   ["-v" "--var SYMBOL" "Symbol indicating the fully qualified name of a specific test."
    :parse-fn symbol
    :assoc-fn accumulate]
   ["-i" "--include KEYWORD" "Run only tests that have this metadata keyword."
    :parse-fn parse-kw
    :assoc-fn accumulate]
   ["-e" "--exclude KEYWORD" "Exclude tests with this metadata keyword."
    :parse-fn parse-kw
    :assoc-fn accumulate]
   ["-H" "--test-help" "Display this help message"]
   ])


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
          (println "opts=%s" opts)
          (System/exit 0))))))
