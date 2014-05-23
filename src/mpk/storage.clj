(ns mpk.storage
  (:import (java.security MessageDigest)
           (java.io File)))

(def file-extension ".data")

(defn md5
  "Generate a md5 checksum for the given string"
  [token]
  (let [hash-bytes
         (doto (java.security.MessageDigest/getInstance "MD5")
               (.reset)
               (.update (.getBytes token)))]
       (let [unpaded (.toString
         (new java.math.BigInteger 1 (.digest hash-bytes)) 16)]
          (if (> (mod (count unpaded) 2) 0)
            (str "0" unpaded)
            unpaded))))


(defn save
  "Saves Kanban with Key to a specific location on the file System"
  [storage-directory kanban-key kanban-content]
  (spit
   (str storage-directory "/" kanban-key file-extension) kanban-content) :encoding "UTF-8")


(defn load-kanban
  "Loads content of Kanban from local file System"
  [storage-directory kanban-key]
  (slurp (.getAbsolutePath (File. storage-directory (str kanban-key file-extension))) :encoding "UTF-8"))

(defn last-updated
  "Checks the last updated timestamp on a file. Returns TIME in ms. 0 if there is no file"
  [storage-directory kanban-key]
  (let [key-file (File. storage-directory (str kanban-key file-extension))]
    (if (.exists key-file)
      (.lastModified key-file)
      0)))
