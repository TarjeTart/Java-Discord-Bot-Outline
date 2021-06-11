public class MessageRecieved {

	private GuildMessageReceivedEvent event;
	private User author; // The user that sent the message
	private Message message; // The message that was received.
	private MessageChannel channel; // channel message was sent in
	private Guild guild; // guild message was sent in
	private TextChannel textChannel; // The TextChannel that this message was sent to
	private Member member; // This Member that sent the message. Contains Guild specific information about
							// the User!
	private String name, msg;
	Group group; // gets group info
	boolean bot; // tells if message is from bot
	private JDA jda;
	long responseNumber;

	public MessageRecieved(GuildMessageReceivedEvent event) {
		this.event = event;
    
    //gets all information from the message
		jda = event.getJDA(); // JDA, the core of the api.
		responseNumber = event.getResponseNumber(); // The amount of discord events that JDA has received since the last
													// reconnect.

		author = event.getAuthor();
		message = event.getMessage();
		channel = event.getChannel();

		msg = message.getContentDisplay(); // String version of message
		bot = author.isBot();

		getInfo();
		
		checkMessage();//instructions to check when recieving a message

	}
	
	private void checkMessage() {
    if(msg.equals("can also use .startsWith()")) {
			EmbedBuilder eb = new EmbedBuilder();//this is used to make embedded messages
			eb.setColor(Color.RED).setTitle("Commands");
			eb.addField("Field title", "Field text", false);//the boolean is for inline text
			channel.sendMessage(eb.build()).queue();//the .queue() is very important
		}
	}
	
 	private void getInfo() {

		
			guild = event.getGuild();
			textChannel = event.getChannel();
			member = event.getMember();

			if (message.isWebhookMessage()) {
				name = author.getName(); // If this is a Webhook message, then there is no Member associated
			} // with the User, thus we default to the author for name.
			else {
				name = member.getEffectiveName(); // This will either use the Member's nickname if they have one,
			} // otherwise it will default to their username. (User#getName())

			System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);

	}

}
