(ns guestbook.db
  (:require [next.jdbc.sql :as sql]
            [next.jdbc :as jdbc]))

(def db-spec {:dbtype "h2" :dbname "data"})
(def datasource (jdbc/get-datasource db-spec))

(defn add-message-to-db
  "Adds a message into the guestbook."
  [username message]
  (let [results (sql/insert! datasource :guestbook_data {:username username
                                                         :message message})]
    (assert (and (map? results) (:GUESTBOOK_DATA/ID results)))
    results))

(defn get-all-messages
  "Gets all messages written into the guestbook."
  []
  (sql/query datasource ["select id, username, message from guestbook_data"]))
