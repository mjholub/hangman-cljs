(ns cljs.hangman-cljs.core
  (:require [reagent.core :as reagent]
            [reagent.dom :as dom]))

;; Holds the game states, e.g. messages, word, guesses, etc."
(defonce state
  (reagent/atom  {:messages []
                  :word "hangman"
                  :incorrect-guesses 0
                  :max-incorrect 6
                  :game-over false
                  :peer-conn nil
                  :data-chan nil}))

(defn handle-message
  "Handles messages from the other player."
  [message]
  (prn "Received message:" message)
  (try
    (let [msg (.-data message)]
      (swap! state update-in [:messages] conj msg))
    (catch js/Error e
      (swap! state update-in [:messages] conj "Failed to handle message." e))))

(defn send-message
  "Sends a message to the other player."
  [peer-conn message]
  (try
    (.send peer-conn message)
    (catch js/Error e
      (swap! state update-in [:messages] conj "Failed to send message." e))))

(defn on-open
  "Handles the peer connection open event."
  [peer-conn]
  (prn "Connection open.")
  (send-message peer-conn "Hello, peer!"))

(defn connect-peer
  "Connects to a peer with the given ID."
  [peer-id]
  (let [peer-conn (js/Peer. peer-id {:key "peerjs", :debug 3})]
    (set! (.-on peer-conn "open") #(on-open peer-conn))
    (set! (.-on peer-conn "data") #(handle-message %))))

(defn get-version-tag []
  (let [version-tag (.-version js/cljs-hangman.core)]
    (if version-tag
      version-tag
      "unknown")))

(defn mount-root []
  (dom/render
   [:div
    [:h1 "P2P Hangman"]
    [:p "Version: " (get-version-tag)]
    ; get the peer ID from the URL
    [:p "Your peer ID: " (.-id (js/Peer. {:key "peerjs", :debug 3}))]
    ; get the other player's id from input field
    [:p "Enter a peer ID to connect to:"]
    [:input {:type "text" :id "peer-id"}]
    [:button {:on-click #(connect-peer (.-value (js/document.getElementById "peer-id")))} "Connect"]
    [:div
     [:h2 "Messages"]
     [:ul
      (for [msg @state]
        ^{:key msg} [:li msg])]]
    [:div
     [:h2 "Game"]
     [:p "TODO: Game goes here."]]
    [:div
     [:h2 "Debug"]
     [:pre (pr-str @state)]]]
   (.getElementById js/document "app")))

(defn init! []
  (mount-root))
