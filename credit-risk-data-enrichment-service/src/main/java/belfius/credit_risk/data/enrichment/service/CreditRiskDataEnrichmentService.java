package belfius.credit_risk.data.enrichment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.lang.model.util.ElementScanner6;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Validator;

import belfius.credit_risk.data.enrichment.entity.Rule;

@ApplicationScoped
public class CreditRiskDataEnrichmentService {
    
    @Inject
    EntityManager entityManager;

    private final Validator validator;

    public CreditRiskDataEnrichmentService(Validator validator) {
        this.validator = validator;
    }

    @Transactional
    public Rule createRule(Rule rule) {

        validator.validate(rule);

        List<Rule> ruleList = entityManager.createQuery("from Rule where id = :id order by version desc", Rule.class).setParameter("id",  rule.getId()).getResultList();
        
        if(ruleList.size() > 0) {
            rule.setVersion(ruleList.get(0).getVersion() + 1);
            rule.setStatusId(1);
        } 
        else {
            rule.setVersion(1);
            rule.setStatusId(1);
        }

        entityManager.persist(rule);
        entityManager.flush();
        return rule;
    }

    public Rule getRule(int id) {

        List<Rule> ruleList = entityManager.createQuery("from Rule where id = :id order by version desc", Rule.class).setParameter("id",  id).getResultList();
        if(ruleList.size() > 0)
            return ruleList.get(0);
        else
            return null;
    }

    @Transactional
    public Rule updateRuleStatus(int id, int status) {
        List<Rule> ruleList = entityManager.createQuery("from Rule where id = :id order by version desc", Rule.class).setParameter("id",  id).getResultList();

        if(ruleList.size() == 0) {
            return null;
        }

        Rule newRule = ruleList.get(0);

        newRule.setStatusId(status);
        entityManager.merge(newRule);
        entityManager.flush();

        return newRule;
    }

    @Transactional
    public Rule updateRule(Rule rule) {
        List<Rule> ruleList = entityManager.createQuery("from Rule where id = :id order by version desc", Rule.class).setParameter("id",  rule.getId()).getResultList();

        if(ruleList.size() == 0) {
            return null;
        }

        Rule newRule = ruleList.get(0);

        newRule.setStatusId(rule.getStatusId());
        newRule.setFamilyId(rule.getFamilyId());
        newRule.setPriority(rule.getPriority());
        newRule.setScopeId(rule.getScopeId());
        newRule.setSentence(rule.getSentence());
        newRule.setDescription(rule.getDescription());

        entityManager.merge(newRule);
        entityManager.flush();

        return newRule;
    }

    public List<Rule> getAllRules(boolean onlyLastVersions) {
        List<Rule> ruleList = new ArrayList<Rule>();
        
        ruleList = entityManager.createQuery("from Rule", Rule.class).getResultList();

        if(onlyLastVersions) {

            List<Rule> modifiedRuleList = new ArrayList<Rule>();

            Map<Integer, Integer> mapRuleId = new HashMap<Integer, Integer>();
            for (Rule rule : ruleList) {
                int id  = rule.getId();
                int version = rule.getVersion();

                if(mapRuleId.get(id) == null) {
                    mapRuleId.put(id, version);
                }
                else {
                    if(mapRuleId.get(id) < version) {
                        mapRuleId.remove(id);
                        mapRuleId.put(id, version);
                    }
                }
            }

            for (Rule rule : ruleList) {
                if(mapRuleId.get(rule.getId()) == rule.getVersion())
                modifiedRuleList.add(rule);
            }

            return modifiedRuleList;
        }

        return ruleList;
    }

    public int validateRuleSyntax(String sentence) {

        if(sentence == null || sentence.isEmpty())
            return -1;

        if(sentence.toLowerCase().startsWith("update")) {
            if(!sentence.contains("set") || !sentence.contains("where")) {
                return -1;
            }

            return 5;
        }
        else if(sentence.toLowerCase().startsWith("delete")) {
            if(!sentence.contains("from") || !sentence.contains("where")) {
                return -1;
            }

            return 30;
        }

        return -1;
    }
}
