package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.SchoolLeagueException;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import requests.AddFootballClubRequest;
import requests.AddMatchRequest;
import responses.LeagueTableResponse;
import responses.SchoolStatsResponse;
import services.SchoolLeagueManager;
import utils.PremierLeagueUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.*;
import static play.mvc.Results.badRequest;

public class SchoolLeagueController {

    private HttpExecutionContext httpExecutionContext;

    @Inject
    SchoolLeagueManager schoolLeagueManager;

    @Inject
    public SchoolLeagueController(HttpExecutionContext httpExecutionContext){
        this.httpExecutionContext = httpExecutionContext;
    }

    public CompletionStage<Result> createSeason(String season, Http.Request request){
        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                schoolLeagueManager.createSeason(seasonYear);
                return created(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Season Created Successfully."));
            } catch (SchoolLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> listSeasons(Http.Request request){

        return supplyAsync(()->{
            List<String> res = schoolLeagueManager.listSeasons();
            if(!res.isEmpty()){
                return ok(Json.toJson(res));
            }
            else {
                return badRequest(PremierLeagueUtil.returnJsonError("-1", "No Seasons Available"));
            }
        },httpExecutionContext.current());

    }

    public CompletionStage<Result> createPremierLeagueClub(String season, Http.Request request){
        JsonNode json = request.body().asJson();

        return supplyAsync(() ->{
            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            if(json == null){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Request Body is Empty");
                return badRequest(jsonNode);
            }

            try {
                schoolLeagueManager.createAndAddFootballClub(seasonYear, Json.fromJson(json, AddFootballClubRequest.class));
                return created(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Club Created Successfully."));
            } catch (SchoolLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> deletePremietLeagueClub(String season, String name, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            if(name == null){
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", "Name Cannot Be Empty"));
            }
            if(name.isEmpty() || name.isBlank()){
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", "Name Cannot Be Empty"));
            }

            try {
                schoolLeagueManager.deleteFootballClub(seasonYear, name);
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Club Deleted Successfully."));
            } catch (SchoolLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> displayStatsPremierLeagueClub(String season, String name, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            if(name == null){
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", "Name Cannot Be Empty"));
            }
            if(name.isEmpty() || name.isBlank()){
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", "Name Cannot Be Empty"));
            }

            try {
                SchoolStatsResponse response = schoolLeagueManager.displayStats(seasonYear, name);
                return ok(Json.toJson(response));
            } catch (SchoolLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> displayPremierLeagueTable(String season, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                List<LeagueTableResponse> response = schoolLeagueManager.displayPremierLeagueTable(seasonYear);
                return ok(Json.toJson(response));
            } catch (SchoolLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        },httpExecutionContext.current());

    }

    public CompletionStage<Result> addMatch(String season, Http.Request request){

        JsonNode json = request.body().asJson();

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            if(json == null){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Request Body is Empty");
                return badRequest(jsonNode);
            }

            try {
                schoolLeagueManager.addMatch(seasonYear, Json.fromJson(json, AddMatchRequest.class));
                return created(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Match Successfully Added."));
            } catch (SchoolLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> testLoad(String season, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                schoolLeagueManager.testLoad(seasonYear);
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Succesfully Loaded"));
            } catch (SchoolLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> getFootballClubs(String season, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                Set<String> footballClubs = schoolLeagueManager.getFootBallClubs(seasonYear);
                return ok(Json.toJson(footballClubs));
            } catch (SchoolLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> loadState(Http.Request request){

        return supplyAsync(()->{
            try {
                schoolLeagueManager.loadState();
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCESS", "State Loaded Successfully"));
            } catch (SchoolLeagueException e) {
                return badRequest(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }
        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> saveState(Http.Request request){

        return supplyAsync(()->{

            try {
                schoolLeagueManager.saveState();
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCESS", "State Saved Successfully"));
            } catch (SchoolLeagueException e) {
                return badRequest(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

}
