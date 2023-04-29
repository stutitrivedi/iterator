import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        User alice = new User("Alice");
        User bob = new User("Bob");
        User Charlie = new User("Charlie");

        ChatHistory chatHistory = new ChatHistory();

        chatHistory.addMessage(new Message(alice, bob, "Hi Bob, how are you?", new Date()));
        chatHistory.addMessage(new Message(bob, alice, "I'm good, thanks for asking. How about you?", new Date()));
        chatHistory.addMessage(new Message(alice, bob, "I'm doing well too, thanks. Do you want to grab lunch later?", new Date()));
        chatHistory.addMessage(new Message(bob, alice, "Sure, where do you want to go?", new Date()));
        chatHistory.addMessage(new Message(alice, bob, "How about that new sushi place on Main Street?", new Date()));
        chatHistory.addMessage(new Message(bob, alice, "Sounds good to me. What time?", new Date()));
        chatHistory.addMessage(new Message(alice, bob, "How about 12:30?", new Date()));
        chatHistory.addMessage(new Message(bob, alice, "Perfect, see you then!", new Date()));
        chatHistory.addMessage(new Message(alice, Charlie, "Hi Charlie, Do you want to join us ?", new Date()));
        chatHistory.addMessage(new Message(Charlie, bob, "I am coming with you guys", new Date()));


        // Iterate over all messages
        System.out.println("Iterating over all messages:");
        for (Message message : chatHistory) {
            System.out.println(message);
        }

        // Iterate over Alice's messages
        System.out.println("\nIterating over Alice's messages: ");
        for (Iterator<Message> it = alice.searchMessagesByUser(chatHistory); it.hasNext(); ) {
            Message message = it.next();
            System.out.println(message);
        }

        System.out.println("\nIterating over Charlie's messages: ");
        for (Iterator<Message> it = Charlie.searchMessagesByUser(chatHistory); it.hasNext(); ) {
            Message message = it.next();
            System.out.println(message);
        }

        // Iterate over Bob's messages
        System.out.println("\nIterating over Bob's messages: ");
        for (Iterator<Message> it = bob.searchMessagesByUser(chatHistory); it.hasNext(); ) {
            Message message = it.next();
            System.out.println(message);
        }


    }

    public static class ChatHistory implements Iterable<Message>, IterableByUser {
        private List<Message> messages;

        public ChatHistory() {
            this.messages = new ArrayList<>();
        }

        public void addMessage(Message message) {
            messages.add(message);
        }

            @Override
        public Iterator<Message> iterator() {
            return messages.iterator();
        }

        @Override
        public Iterator<Message> iterator(User userToSearchWith) {
            return new MessageIterator(userToSearchWith);
        }

        private class MessageIterator implements Iterator<Message> {
            private int currentIndex;
            private User userToSearchWith;

            public MessageIterator(User userToSearchWith) {
                this.currentIndex = 0;
                this.userToSearchWith = userToSearchWith;
            }

            @Override
            public boolean hasNext() {
                while (currentIndex < messages.size()) {
                    Message message = messages.get(currentIndex);
                    if (message.getSender().equals(userToSearchWith) || message.getReceiver().equals(userToSearchWith)) {
                        return true;
                    }
                    currentIndex++;
                }
                return false;
            }

            @Override
            public Message next() {
                Message message = messages.get(currentIndex);
                currentIndex++;
                return message;
            }
        }
    }

    public static class User implements Iterable<Message> {
        private String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public Iterator<Message> iterator() {
            throw new UnsupportedOperationException("Cannot iterate over messages without specifying ChatHistory.");
        }

        public Iterator<Message> searchMessagesByUser(ChatHistory chatHistory) {
            return chatHistory.iterator(this);
        }
    }

    public static class Message {

        private User sender;
        private User receiver;
        private String text;
        private Date timestamp;

        public Message(User sender, User receiver, String text, Date timestamp) {
            this.sender = sender;
            this.receiver = receiver;
            this.text = text;
            this.timestamp = timestamp;
        }

        public User getSender() {
            return sender;
        }

        public User getReceiver() {
            return receiver;
        }

        public String getText() {
            return text;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return timestamp + " [" + sender.getName() + " -> " + receiver.getName() + "]: " + text;
        }
    }

    public interface IterableByUser {
        default Iterator<Message> iterator() {
            return iterator(null);
        }

        Iterator<Message> iterator(User userToSearchWith);
    }
}

