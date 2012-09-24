package co.uk.flansmods.common.teams;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Packet;
import net.minecraft.src.Vec3;

import co.uk.flansmods.common.FlansMod;
import co.uk.flansmods.common.FlansModPlayerData;
import co.uk.flansmods.common.FlansModPlayerHandler;
import co.uk.flansmods.common.network.PacketTeamSelect;

public abstract class Gametype {
	
	public static List<Gametype> gametypes = new ArrayList<Gametype>();
	public static TeamsManager teamsManager = TeamsManager.getInstance();
	
	public static Gametype getGametype(String type)
	{
		for(Gametype gametype : gametypes)
		{
			if(gametype.shortName.equals(type))
				return gametype;
		}
		return null;
	}
	
	public String name;
	public String shortName;
	public int numTeamsRequired;
	
	public Gametype(String s, String s1, int numTeams)
	{
		name = s;
		shortName = s1;
		numTeamsRequired = numTeams;
		gametypes.add(this);
	}
	
	public static FlansModPlayerData getPlayerData(EntityPlayerMP player)
	{
		return FlansModPlayerHandler.getPlayerData(player);
	}
	
	public static void sendPacketToPlayer(Packet packet, EntityPlayerMP player)
	{
		player.serverForThisPlayer.sendPacketToPlayer(packet);
	}
	
	public static void sendTeamsMenuToPlayer(EntityPlayerMP player)
	{
		Team[] availableTeams = new Team[teamsManager.teams.length + 1];
		for(int i = 0; i < teamsManager.teams.length; i++)
		{
			availableTeams[i] = teamsManager.teams[i];
		}
		availableTeams[teamsManager.teams.length + 1] = teamsManager.spectators;
		
		getPlayerData(player).team = teamsManager.spectators;
		sendPacketToPlayer(PacketTeamSelect.buildTeamChoicesPacket(availableTeams), player);
	}

	public abstract void initGametype();
	
	public abstract void startNewRound();
	
	public abstract void stopGametype();
	
	public abstract void tick();
	
	public abstract void playerJoined(EntityPlayerMP player);
	
	public abstract void playerQuit(EntityPlayerMP player);
	
	public abstract void playerChoseTeam(EntityPlayerMP player, Team team);
	
	public abstract void playerChoseClass(EntityPlayerMP player, PlayerClass playerClass);
	
	//Return true if damage should be dealt.
	public abstract boolean playerAttacked(EntityPlayerMP player, DamageSource source);
	
	public abstract void playerKilled(EntityPlayerMP player, DamageSource source);
	
	public abstract void baseAttacked(ITeamBase base, DamageSource source);
	
	public abstract void objectAttacked(ITeamObject object, DamageSource source);

	public abstract void baseClickedByPlayer(ITeamBase base, EntityPlayerMP player);
	
	public abstract void objectClickedByPlayer(ITeamObject object, EntityPlayerMP player);
	
	public abstract Vec3 getSpawnPoint(EntityPlayerMP player);
}
