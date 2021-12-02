package com.adcubum.timerecording.proles.ticketbacklog;

import com.adcubum.timerecording.jira.data.ticket.IssueType;
import com.adcubum.timerecording.jira.data.ticket.Ticket;
import com.adcubum.timerecording.jira.data.ticket.TicketActivity;
import com.adcubum.timerecording.jira.data.ticket.TicketAttrs;
import com.adcubum.timerecording.proles.ticketbacklog.read.data.ProlesTicketJsonImport;

import java.util.List;
import java.util.Objects;

import static com.adcubum.util.utils.StringUtil.isNotEmptyOrNull;
import static java.util.Objects.requireNonNull;

public class ProlesTicketImpl implements Ticket {

    private List<TicketActivity> ticketActivities;
    private TicketAttrs ticketAttrs;

    private ProlesTicketImpl(String ticketNr, String customer, String project, String description, List<TicketActivity> ticketActivities) {
        this.ticketActivities = ticketActivities;
        this.ticketAttrs = new ProlesTicketAttr(ticketNr, customer, project,  description);
    }

    @Override
    public List<TicketActivity> getTicketActivities() {
        return ticketActivities;
    }

    @Override
    public String toString() {
        return "ProlesTicketImpl{" +
                "ticketRepresentation='" + getTicketRep() + '\'' +
                ", ticketAttrs=" + ticketAttrs +
                '}';
    }

    @Override
    public String getTicketRep() {
        return getNr() + " (" + ticketAttrs.getTitle() + ")";
    }

    @Override
    public boolean isCurrentUserAssigned() {
        return false;
    }

    @Override
    public boolean isSprintTicket() {
        return false;
    }

    @Override
    public boolean isDummyTicket() {
        return false;
    }

    @Override
    public boolean isBookable() {
        ProlesTicketAttr prolesTicketAttr = (ProlesTicketAttr) this.ticketAttrs;
        return isNotEmptyOrNull(prolesTicketAttr.ticketNr)
                && isNotEmptyOrNull(prolesTicketAttr.project)
                && !ticketActivities.isEmpty();
    }

    @Override
    public String getNr() {
        return ticketAttrs.getNr();
    }

    @Override
    public TicketAttrs getTicketAttrs() {
        return ticketAttrs;
    }

    public static ProlesTicketImpl of(ProlesTicketJsonImport prolesTicketJsonImport) {
        return new ProlesTicketImpl(prolesTicketJsonImport.getTicketNr(), prolesTicketJsonImport.getCustomer(), prolesTicketJsonImport.getProject()
                , prolesTicketJsonImport.getDescription(), prolesTicketJsonImport.getProlesActivity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProlesTicketImpl that = (ProlesTicketImpl) o;
        return Objects.equals(ticketActivities, that.ticketActivities) && Objects.equals(ticketAttrs, that.ticketAttrs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketActivities, ticketAttrs);
    }

    public static final class ProlesTicketAttr implements TicketAttrs {

        private String ticketNr;
        private String title;
        private String project;
        private String customer;

        private ProlesTicketAttr(String ticketNr, String customer, String project, String title) {
            this.ticketNr = requireNonNull(ticketNr);
            this.customer = requireNonNull(customer);
            this.project = requireNonNull(project);
            this.title = requireNonNull(title);
        }

        @Override
        public String toString() {
            return "ProlesTicketAttr{" +
                    "ticketNr='" + ticketNr + '\'' +
                    ", title='" + title + '\'' +
                    ", project='" + project + '\'' +
                    ", customer='" + customer + '\'' +
                    '}';
        }

        @Override
        public String getNr() {
            return ticketNr;
        }

        @Override
        public String getExternalNr() {
            return null;
        }

        @Override
        public String getEpicNr() {
            return null;
        }

        @Override
        public IssueType getIssueType() {
            return IssueType.UNKNOWN;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getImplementationPackage() {
            return null;
        }

        @Override
        public long getProjectNr() {
            return -1;
        }

        @Override
        public int getProjectCostUnit() {
            return -1;
        }

        @Override
        public String getProjectDesc() {
            return project;
        }

        @Override
        public String getThema() {
            return customer;
        }

        @Override
        public String getSubthema() {
            return null;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getBusinessTeamPlaning() {
            return null;
        }

        @Override
        public String getPlaningId() {
            return null;
        }

        @Override
        public String getSyriusExtension() {
            return null;
        }

        @Override
        public String getSyriusRelease() {
            return null;
        }

        @Override
        public String getAssignee() {
            return null;
        }

        @Override
        public String getSprintId() {
            return null;
        }

        @Override
        public String getSprintName() {
            return null;
        }

        @Override
        public boolean isBookable() {
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProlesTicketAttr that = (ProlesTicketAttr) o;
            return Objects.equals(ticketNr, that.ticketNr) && Objects.equals(title, that.title) && Objects.equals(project, that.project) && Objects.equals(customer, that.customer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ticketNr, title, project, customer);
        }
    }
}
