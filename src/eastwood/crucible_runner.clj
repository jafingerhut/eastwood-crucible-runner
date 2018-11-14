(ns eastwood.crucible-runner
  (:import (java.io PushbackReader)
           (clojure.lang LineNumberingPushbackReader))
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :as cli]
            [clojure.edn :as edn]
            [clojure.pprint :as pp]
            [clojure.string :as str]))


(defn read-version-info [fname]
  (try
    (edn/read (java.io.PushbackReader. (io/reader fname)))
    (catch java.io.FileNotFoundException e
      (println (format "No such file: %s" fname))
      (System/exit 1))
    (catch Throwable e
      (println e)
      (println (format "Error while attempting to read file: %s" fname))
      (System/exit 1))))


(defn crucible-runner [options]
  ;;(println (format "options=%s" options))
  (let [versions-file (:versions options)
        versions-info (read-version-info versions-file)]
    (pp/pprint versions-info)))


(def cli-options
  [["-v" "--versions FILENAME" "Data file describing combination of (Eastwood, Clojure, JDK) versions to run"]
   ["-H" "--test-help" "Display this help message"]])


(defn- help [summary]
  (println "\nUsage:\n")
  (println "clj -m" (namespace `help) "<options>\n")
  (println summary))


(defn -main
  "Entry point for the Eastwood crucible test runner"
  [& args]
  (let [{:keys [errors summary options] :as args}
        (cli/parse-opts args cli-options)]
    ;;(println (format "args=%s" args))
    (cond
      errors (do (doseq [e errors]
                   (println e))
                 (help summary))
      (:test-help options) (help summary)
      :else (crucible-runner options))))
