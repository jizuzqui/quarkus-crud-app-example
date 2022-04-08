package belfius.credit_risk.data.enrichment.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import belfius.credit_risk.data.enrichment.dto.RowsAffected;
import belfius.credit_risk.data.enrichment.dto.Status;
import belfius.credit_risk.data.enrichment.entity.Rule;
import belfius.credit_risk.data.enrichment.service.CreditRiskDataEnrichmentService;

@Path("/rule")
public class CreditRiskDataEnrichmentResource {
 
    private Logger logger;
    private CreditRiskDataEnrichmentService crDataEnrichmentService;

    public CreditRiskDataEnrichmentResource(Logger logger, CreditRiskDataEnrichmentService crDataEnrichmentService) {
        this.logger = logger;
        this.crDataEnrichmentService = crDataEnrichmentService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<belfius.credit_risk.data.enrichment.dto.Rule> getRules(@QueryParam("onlyLastVersion") boolean onlyLastVersion) {

        logger.info("Retrieving rule list...");

        List<Rule> ruleEntityList = crDataEnrichmentService.getAllRules(onlyLastVersion);
        List<belfius.credit_risk.data.enrichment.dto.Rule> ruleList = new ArrayList<belfius.credit_risk.data.enrichment.dto.Rule>();

        for (Rule rule : ruleEntityList) {
            ruleList.add(new belfius.credit_risk.data.enrichment.dto.Rule(
                rule.getId(),
                rule.getVersion(),
                rule.getStatusId(),
                rule.getFamilyId(),
                rule.getScopeId(),
                rule.getPriority(),
                rule.getSentence(),
                rule.getDescription()));
            
        }

        logger.info("Rule list retrieved with " + ruleList.size() + " hits");
        logger.info("end");

        return ruleList;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public belfius.credit_risk.data.enrichment.dto.Rule getRule(@PathParam("id") int id) {

        logger.info("Retrieving rule....");

        Rule rule = crDataEnrichmentService.getRule(id);
        if(rule == null)
            throw new WebApplicationException(404);

        belfius.credit_risk.data.enrichment.dto.Rule dto = new belfius.credit_risk.data.enrichment.dto.Rule(
            rule.getId(),
            rule.getVersion(),
            rule.getStatusId(),
            rule.getFamilyId(),
            rule.getScopeId(),
            rule.getPriority(),
            rule.getSentence(),
            rule.getDescription());

        logger.info("end");

        return dto;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Creates a new rule"
    )
    @APIResponse(
        responseCode = "200",
        description = "Rule successfully created",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = URI.class))
    )
    public Rule createRule(@Valid @NotNull belfius.credit_risk.data.enrichment.dto.Rule ruleDTO, @Context UriInfo uriInfo) {

        Rule rule = new Rule();
        rule.setId(ruleDTO.getId());
        rule.setFamilyId(ruleDTO.getFamilyId());
        rule.setScopeId(ruleDTO.getScopeId());
        rule.setPriority(ruleDTO.getPriority());
        rule.setStatusId(ruleDTO.getStatusId());
        rule.setSentence(ruleDTO.getSentence());
        rule.setDescription(ruleDTO.getDescription());

        rule = crDataEnrichmentService.createRule(rule);

        var builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(rule.getId()) + "-" + Long.toString(rule.getVersion()));

        return rule;
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Updates the status of an existing rule"
    )
    @APIResponse(
        responseCode = "200",
        description = "Rule successfully created",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = URI.class))
    )
    @Path("/{id}/status")
    public Rule updateRuleStatus(@PathParam("id") int id, @Valid @NotNull Status status) {

        Rule rule = crDataEnrichmentService.updateRuleStatus(id, status.getStatus());

        return rule;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Updates an existing rule"
    )
    @APIResponse(
        responseCode = "204",
        description = "Rule successfully updated",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = URI.class))
    )
    @Path("/{id}")
    public Response updateRule(@PathParam("id") int id, @Valid @NotNull belfius.credit_risk.data.enrichment.dto.Rule ruleDTO) {
        
        Rule rule = new Rule();

        rule.setId(id);
        rule.setFamilyId(ruleDTO.getFamilyId());
        rule.setScopeId(ruleDTO.getScopeId());
        rule.setPriority(ruleDTO.getPriority());
        rule.setStatusId(ruleDTO.getStatusId());
        rule.setSentence(ruleDTO.getSentence());
        rule.setDescription(ruleDTO.getDescription());

        rule = crDataEnrichmentService.updateRule(rule);

        if(rule == null)
            throw new WebApplicationException(404);

        return Response.noContent().build();
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Validate syntax of a rule"
    )
    @APIResponse(
        responseCode = "200",
        description = "Syntax is valid",
        content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = URI.class))
    )
    @Path("/{id}/syntax")
    public Response validateRuleSyntax(@Valid @NotNull belfius.credit_risk.data.enrichment.dto.Rule rule) {

        int rowsAffected = crDataEnrichmentService.validateRuleSyntax(rule.getSentence());

        if(rowsAffected == -1)
            throw new WebApplicationException(400);


        RowsAffected rowsAffectedDTO = new RowsAffected();
        rowsAffectedDTO.setRowsAffected(rowsAffected);

        return Response.ok(rowsAffectedDTO).build();
    }
}