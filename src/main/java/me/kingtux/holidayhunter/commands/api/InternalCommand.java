package me.kingtux.holidayhunter.commands.api;

import me.kingtux.simpleannotation.AnnotationFinder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InternalCommand extends Command {
    private IkeaCommand ikeaCommand;
    private InternalBaseCommand internalBaseCommand;
    private List<InternalOtherCommand> internalOtherCommands;
    private List<InternalSubCommand> internalSubCommands;

    /**
     * This creates the command
     *
     * @param ikeaCommand  The Command
     * @param commandRules the rules this is for easier and cleaner access
     */
    protected InternalCommand(IkeaCommand ikeaCommand, IkeaCommandRules commandRules) {

        super(commandRules.command(), commandRules.description(), commandRules.format(), Arrays.asList(commandRules.aliases()));
        this.ikeaCommand = ikeaCommand;

        if (AnnotationFinder.getMethodsWithAnnotation(ikeaCommand.getClass(), BaseCommand.class).length >= 2) {
            throw new IllegalArgumentException("An IkeaCommand may not have more than 1 BaseCommand");
        }
        loadInternalBaseCommand();
        loadInternalSubCommands();
        loadOther();

    }

    private Method getBaseTab() {
        for (Method m : AnnotationFinder.getMethodsWithAnnotation(ikeaCommand.getClass(), TabCompleter.class)) {
            if (m.getAnnotation(TabCompleter.class).type() == TabCompleter.TYPE.BASE) {
                return m;
            }
        }
        return null;
    }

    private void loadInternalBaseCommand() {
        Method method = AnnotationFinder.getMethodsWithAnnotation(ikeaCommand.getClass(), BaseCommand.class)[0];
        System.out.println(method.getName());
        System.out.println(method.toGenericString());
        Method tabMethod = getBaseTab();
        TabCompleter tabCompleter = null;
        if (tabMethod != null) {
            tabCompleter = tabMethod.getAnnotation(TabCompleter.class);
        }
        internalBaseCommand = new InternalBaseCommand(method.getAnnotation(BaseCommand.class), method, tabMethod, ikeaCommand);

    }

    private void loadInternalSubCommands() {
        internalSubCommands = new ArrayList<>();
        for (Method method : AnnotationFinder.getMethodsWithAnnotation(ikeaCommand.getClass(), SubCommand.class)) {
            Method tabMethod = getSubTab(method.getAnnotation(SubCommand.class).subcommand());
            TabCompleter tabCompleter = null;
            if (tabMethod != null) {
                tabCompleter = tabMethod.getAnnotation(TabCompleter.class);
            }
            internalSubCommands.add(new InternalSubCommand(method.getAnnotation(SubCommand.class), tabCompleter, method, tabMethod, ikeaCommand));
        }
    }

    private Method getSubTab(String sub) {
        for (Method m : AnnotationFinder.getMethodsWithAnnotation(ikeaCommand.getClass(), TabCompleter.class)) {
            if (m.getAnnotation(TabCompleter.class).type() == TabCompleter.TYPE.SUB_COMMAND && m.getAnnotation(TabCompleter.class).subCommandToEffect().equalsIgnoreCase(sub)) {
                return m;
            }
        }
        return null;
    }

    private void loadOther() {
        internalOtherCommands = new ArrayList<>();
        for (Method method : AnnotationFinder.getMethodsWithAnnotation(ikeaCommand.getClass(), OtherCommand.class)) {
            internalOtherCommands.add(new InternalOtherCommand(method.getAnnotation(OtherCommand.class), method, ikeaCommand));
        }
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (internalSubCommands.isEmpty()) {
            internalBaseCommand.invoke(args, sender, commandLabel);
            return true;
        }
        if (args.length == 0) {
            internalBaseCommand.invoke(args, sender, commandLabel);
        } else if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("help")) {
                for (InternalOtherCommand internalOtherCommand : internalOtherCommands) {
                    if (internalOtherCommand.getAnnotation().commandType() == OtherCommand.CommandType.HELP) {
                        internalOtherCommand.invoke(args, sender, commandLabel);
                        return true;
                    } else {
                        //Lets Make an our own help command :)!
                        sender.sendMessage(getDefaultHelp());
                    }
                }
            }
            for (InternalSubCommand internalSubCommand : internalSubCommands) {
                List<String> d = Arrays.asList(args);
                d.remove(0);
                internalSubCommand.invoke(d.toArray(new String[0]), sender, commandLabel);
            }
        } else {
            for (InternalOtherCommand internalOtherCommand : internalOtherCommands) {
                if (internalOtherCommand.getAnnotation().commandType() == OtherCommand.CommandType.FALL_BACK) {
                    internalOtherCommand.invoke(args, sender, commandLabel);
                }
            }
        }
        return true;
    }

    private List<String> tab(CommandSender sender, String alias, String[] args) {
        System.out.println("Called");
        System.out.println(args.length);
        Arrays.stream(args).forEach(System.out::println);
        if (args.length == 1) {
            System.out.println("Base COmmand");
            if (internalBaseCommand.getTabCompleter() == null) {
                System.out.println("Using Default Sub Commandor");
                List<String> strings = new ArrayList<>();
                internalSubCommands.forEach(e -> strings.add(e.getAnnotation().subcommand()));
                return strings;
            } else {
                return internalBaseCommand.invokeTab(args, sender, alias);
            }
        } else if (args.length > 1) {
            for (InternalSubCommand internalSubCommand : internalSubCommands) {
                if (internalSubCommand.getTabCompleter() != null && internalSubCommand.getTabCompleterMethod() != null) {
                    if (internalSubCommand.getTabCompleter().subCommandToEffect().equalsIgnoreCase(args[0])) {
                        return internalSubCommand.invokeTab(args, sender, alias);
                    }
                }
            }
        }
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return tab(sender, alias, args);

    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        return tab(sender, alias, args);
    }

    private String[] getDefaultHelp() {
        List<String> lists = new ArrayList<>();
        return lists.toArray(new String[1]);
    }

}
