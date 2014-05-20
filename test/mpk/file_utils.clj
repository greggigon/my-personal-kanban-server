(ns mpk.file-utils
  (:import (java.io File)))

(defn make-folder [folder-path]
  (let [temp-folder (File. folder-path)]
    (if (not (.exists temp-folder))
      (.mkdirs (File. folder-path)))))

(defn clean-folder [folder-path]
  (let [folder (File. folder-path)]
    (if (.exists folder)
      (let [files-in-folder (.listFiles folder)]
        (map files-in-folder #(.delete %1))))))
