package fr.northenflo.permiflow.commands;

import fr.northenflo.permiflow.Main;
import fr.northenflo.permiflow.commands.executors.PGroupExecutor;
import fr.northenflo.permiflow.commands.executors.PPlayerExecutor;
import fr.northenflo.permiflow.commands.executors.PReloadExecutor;

public class PermiFlowCommandManager {

    public PermiFlowCommandManager(final Main instance) {
        instance.getListCommands().add(new PReloadExecutor(instance, "pfreload", "/pfreload","Recharger la configuration de PermiFlow (Requis: Avoir une permission)", "permiflow.reload", false));
        instance.getListCommands().add(new PGroupExecutor(instance, "pfgroup", "/pfgroup", "Action pour les groupes", "", false, 2));
        instance.getListCommands().add(new PPlayerExecutor(instance, "pfplayer", "/pfplayer", "Action pour les joueurs", "permiflow.player.*", false, 3));
        for(ICommandP cmd : instance.getListCommands())
            instance.getCommand(cmd.getName()).setExecutor(cmd);
    }

}
