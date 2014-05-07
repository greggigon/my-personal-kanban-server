(ns my-personal-kanban-local-cloud.storage)

(def file-extension ".data")

(defn save
  "Saves Kanban with Key to a specific location on the file System"
  [storage-directory kanban-key kanban-content]
  (spit
   (str storage-directory "/" kanban-key file-extension) (pr-str kanban-content)))


(defn load-kanban
  "Loads content of Kanban from local file System"
  [storage-directory kanban-key]
  (read-string
   (slurp (str storage-directory "/" kanban-key file-extension))))
