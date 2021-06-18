public class Driver extends ListenerAdapter{

public static void main(String[] args){
        //these two lines create a jda instance and launch it
        JDAMaker jda = new JDAMaker("ODU1MjE4OTExNTM2NzQyNDMw.YMvSrQ.em4mzaYZAcS-a4fqV6fJPJX9ToM","-help");
        //use constructJdaWithMusic for music bots
        jda.constructJda();
    }
    //this an example of a listener for an event, see jda documentation for all events
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        //this is a class that holds instructions for when bot recieves a message
        new MessageRecieved(event);
    }//ends message received event    
}
