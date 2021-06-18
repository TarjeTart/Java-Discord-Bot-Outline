package Music.Testing.Bot;

import java.awt.Color;
import java.net.URI;
import java.net.URISyntaxException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import lavaplayer.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Invite.Group;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class MessageRecievedMusic {

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
		//commands list
		if(msg.equals("-help")) {
			EmbedBuilder eb = new EmbedBuilder();//this is used to make embedded messages
			eb.setColor(Color.RED).setTitle("Commands");
			eb.addField("-play song", "plays song", false);//the boolean is for inline text
			channel.sendMessageEmbeds(eb.build()).queue();//the .queue() is very important
		}
		else if(msg.startsWith("-play ")) {
			String song = msg.replaceFirst("-play ", "");
			if(!isUrl(song)) {
				song  = "ytsearch:" + song;
			}
			Member self = event.getMember().getGuild().getSelfMember();
			GuildVoiceState selfVoiceState = self.getVoiceState();
			GuildVoiceState memberVoiceState = member.getVoiceState();
			AudioManager audioManager = guild.getAudioManager();
			VoiceChannel memberChannel = memberVoiceState.getChannel();
			//bot is not in vc
			if(!selfVoiceState.inVoiceChannel()) {
				//user not in vc
				if(!memberVoiceState.inVoiceChannel()) {
					channel.sendMessage("Join a voice channel to use this command").queue();
					return;
				//bot is not in vc but user is
				}else{
					//join channel
					audioManager.openAudioConnection(memberChannel);
					//add song
					PlayerManager.getInstance().loadAndPlay(textChannel, song);
				}
			//bot is in vc
			}else {
				//user not in vc
				if(!memberVoiceState.inVoiceChannel()) {
					channel.sendMessage("Join a voice channel to use this command").queue();
					return;
				//bot is different vc of user
				}else if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
					//join user vc
					audioManager.openAudioConnection(memberChannel);
					//add song
					PlayerManager.getInstance().loadAndPlay(textChannel, song);
				//bot is in same vc as user
				}else {
					//add song
					PlayerManager.getInstance().loadAndPlay(textChannel, song);
				}
			}
    	}
		//stops current song and clears queue
		else if(msg.equalsIgnoreCase("-stop")) {
			Member self = event.getMember().getGuild().getSelfMember();
			GuildVoiceState selfVoiceState = self.getVoiceState();
			GuildVoiceState memberVoiceState = member.getVoiceState();
			AudioManager audioManager = guild.getAudioManager();
			VoiceChannel memberChannel = memberVoiceState.getChannel();
			//bot is not in vc
			if(!selfVoiceState.inVoiceChannel()) {
				//user not in vc
				if(!memberVoiceState.inVoiceChannel()) {
					channel.sendMessage("Join a voice channel to use this command").queue();
					return;
				//bot is not in vc but user is
				}else{
					channel.sendMessage("bot is not running").queue();
					return;
				}
			//bot is in vc
			}else {
				//user not in vc
				if(!memberVoiceState.inVoiceChannel()) {
					channel.sendMessage("Join a voice channel to use this command").queue();
					return;
				//bot is different vc of user
				}else if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
					channel.sendMessage("bot is a different vc from you").queue();
					return;
				//bot is in same vc as user
				}else {
					//stop bot
					PlayerManager.getInstance().getMusicManager(guild).scheduler.player.stopTrack();
					PlayerManager.getInstance().getMusicManager(guild).scheduler.queue.clear();
					//disconnect
					guild.getAudioManager().closeAudioConnection();;
				}
			}
		}else if(msg.equalsIgnoreCase("-skip")) {
			Member self = event.getMember().getGuild().getSelfMember();
			GuildVoiceState selfVoiceState = self.getVoiceState();
			GuildVoiceState memberVoiceState = member.getVoiceState();
			AudioManager audioManager = guild.getAudioManager();
			VoiceChannel memberChannel = memberVoiceState.getChannel();
			//bot is not in vc
			if(!selfVoiceState.inVoiceChannel()) {
				//user not in vc
				if(!memberVoiceState.inVoiceChannel()) {
					channel.sendMessage("Join a voice channel to use this command").queue();
					return;
				//bot is not in vc but user is
				}else{
					channel.sendMessage("This bot is not playing anything").queue();
					return;
				}
			//bot is in vc
			}else {
				//user not in vc
				if(!memberVoiceState.inVoiceChannel()) {
					channel.sendMessage("Join a voice channel to use this command").queue();
					return;
				//bot is different vc of user
				}else if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
					channel.sendMessage("must be in same vc as bot to use this command").queue();
					return;
				//bot is in same vc as user
				}else {
					//skip
					final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
					final AudioPlayer audioPlayer = musicManager.audioPlayer;
					if(audioPlayer.getPlayingTrack() == null) {
						channel.sendMessage("There is no song playing").queue();
						return;
					}
					musicManager.scheduler.nextTrack();
					channel.sendMessage("Skipped current track").queue();
				}
			}
		}
	}
	
 	private boolean isUrl(String song) {
		try {
			new URI(song);
			return true;
		} catch(URISyntaxException e) {
			return false;
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
