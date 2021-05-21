import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.Event;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.permission.Role;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//import org.javacord.api.entity.webhook;
public class Gateway {
    private static final String[] filter = {"https:","dis", "cord", ".gg", "nig",  "fag", "tranny", "jew",  "kike",  "spic", "black" };
    private static ArrayList<String> blacklist = new ArrayList<String>();
    public static boolean filter(String message)
    {
        for(String f: filter)
        {
                message = message.replace("\n", "").replace("\r", "").replace("    ", "");
                int idx = message.toLowerCase().indexOf(f);
                if(idx != -1 || message.length() > 200)
                    return true; 
        }
        return false;
    }  
    public static void Gateway(DiscordApi api)
    {
        api.addMessageCreateListener(event -> {
        Nameable nameableChannel = (Nameable)event.getChannel();
        Nameable nameableServer = (Nameable)event.getServer().get();
        User userAuthor = event.getMessage().getUserAuthor().get();
        String userId = userAuthor.getIdAsString();
        String message = event.getMessageContent();
        
        boolean auth = true;
        for(int i = 0; i < blacklist.size(); i++)
        {
            if(userId.equals(blacklist.get(i)))
            {
                auth = false;    
            }
        }
        if (
        nameableChannel.getName().compareTo("gateway") == 0 
        && !userAuthor.isBot()
        && !filter(message)
        && auth
        ) 
        {
            Collection<ServerTextChannel> gateway = api.getServerTextChannelsByName("gateway");
            EmbedBuilder embed = new EmbedBuilder()
            .setAuthor(userAuthor)
            .setDescription(message)
            .setFooter(nameableServer.getName() + " " + userId);
            for(ServerTextChannel c: gateway)
            {
                if((Channel)c != (event.getChannel()))
                {       
                       c.sendMessage(embed); 
                       c.deleteMessages(1);
                }
            }
        }
        else if(event.getMessageContent().equalsIgnoreCase(":sUb6Az=C_9Hw,B5"))
        {
            return;    
        }
        });
    }
    public static void main(String[] args) {
        String token = "ODQyOTg2OTA4MjI5MTczMjc4.YJ9SvA.35wu6pu_32uTICBDvWuj0jHcBno";
        
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
        
        api.addMessageCreateListener(event -> {
        Message msg = event.getMessage();
        MessageAuthor msgAuth = msg.getAuthor();
        User user = msgAuth.asUser().get();
        Collection<Role> roles = user.getRoles(event.getServer().get());
        boolean auth = false;
        String admin = "844578955611996230";
        for(Role r: roles)
        {
            if(r.getIdAsString().compareTo(admin) == 0)
            {
                auth = true;
            }
        }
        if (event.getMessageContent().equalsIgnoreCase("%invite")) {
            event.getChannel().sendMessage("https://discord.com/oauth2/authorize?client_id=842986908229173278&scope=bot&permissions=8");
        }
        else if(auth)
            if(event.getMessageContent().equalsIgnoreCase("%exit"))
            {
                event.getChannel().sendMessage(":white_check_mark:");
                System.exit(0);    
            }
            else if(event.getMessageContent().equalsIgnoreCase("%selfDestruct"))
            {
                event.getChannel().sendMessage(":white_check_mark:");
                Collection<Server> servers = api.getServers();   
                for(Server s: servers)
                {
                    s.leave();
                }
            }
            else if(event.getMessageContent().equalsIgnoreCase("%start"))
            {
                Gateway(api);
                event.getChannel().sendMessage(":white_check_mark:");
            }
            else if(event.getMessageContent().indexOf("%mute") != -1)
            {
                String id = event.getMessageContent().substring(6);
                blacklist.add(id);
                event.getChannel().sendMessage(":white_check_mark:");
            }
            else if(event.getMessageContent().indexOf("%unmute") != -1)
            {
                String id = event.getMessageContent().substring(8);
                for(int i = 0; i < blacklist.size(); i++)
                {
                    if(blacklist.get(i).equals(id))
                    {
                        blacklist.remove(i);
                    }
                }
                event.getChannel().sendMessage(":white_check_mark:");
            }
            else if(event.getMessageContent().equalsIgnoreCase("%printArr"))
            {
                for(String s: blacklist)
                {
                    System.out.println(s);
                }
                event.getChannel().sendMessage(":white_check_mark:");
            }
            else if(event.getMessageContent().equalsIgnoreCase("%servers"))
            {
                Collection<Server> servers = api.getServers(); 
                int counter = 0;
                for(Server s: servers)
                {
                    Nameable nameableServer = (Nameable)s;
                    event.getChannel().sendMessage((counter + " " + nameableServer.getName()));
                    counter++;
                }
            }
        });
        }
    }
        
        
        

