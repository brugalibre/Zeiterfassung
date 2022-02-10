<template>
  <div class="flex-center">
    <h2>{{ title }}</h2>
    <div class="zeiterfassung-overview">
      <div id="table-and-bottom">
        <div class="table">
          <table>
            <tr>
              <th id="amountOfHours">Anzahl Stunden</th>
              <th id="ticket" class="ticket-nr-cell">Ticket</th>
              <th id="description" class="descriptionCell">Beschreibung</th>
              <th id="from">Von</th>
              <th id="upto">Bis</th>
              <th id="servicecode" class="service-code-cell">Leistungsart</th>
              <th id="booked">Abgebucht</th>
              <th id="delete">Eintrag löschen</th>
            </tr>
            <tr v-for="businessDayIncrement in businessDay.businessDayIncrementDtos" :key="businessDayIncrement.id"
                v-bind:class="{ isBookedRecord: businessDayIncrement.isBooked}">
              <td>
                <div v-show="!businessDayIncrement.isDurationRepEditable||businessDayIncrement.isBooked">
                  <label
                    @click="businessDayIncrement.isDurationRepEditable=true;">
                    {{ businessDayIncrement.timeSnippetDto.durationRep }}
                  </label>
                </div>
                <input v-show="businessDayIncrement.isDurationRepEditable&&!businessDayIncrement.isBooked"
                       v-model="businessDayIncrement.timeSnippetDto.durationRep"
                       name="durationRep"
                       v-on:blur="businessDayIncrement.isDurationRepEditable=false;"
                       @keydown.esc="businessDayIncrement.isDurationRepEditable=false"
                       @keyup.enter="businessDayIncrement.isDurationRepEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
                />
              </td>
              <td>
                <div v-show="!businessDayIncrement.isTicketNrEditable||businessDayIncrement.isBooked"
                     class="ticket-nr-cell">
                  <label
                    @click="businessDayIncrement.isTicketNrEditable=true;">
                    {{ businessDayIncrement.ticketDto.ticketNr }}
                  </label>
                </div>
                <ticket-nr-input-field
                  v-show="businessDayIncrement.isTicketNrEditable&&!businessDayIncrement.isBooked"
                  v-model="businessDayIncrement.ticketDto.ticketNr"
                  v-bind:initTicketNr="businessDayIncrement.ticketDto.ticketNr"
                  @change="businessDayIncrement.isTicketNrEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
                  @ticketNrChanged="onTicketNrChanged(businessDayIncrement, $event)"
                  v-on:blur="businessDayIncrement.isTicketNrEditable=false"
                  @keydown.esc="businessDayIncrement.isTicketNrEditable=false"
                  @keyup.enter="businessDayIncrement.isTicketNrEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
                >
                </ticket-nr-input-field>
              </td>
              <td class="editableTableCell">
                <div v-show="!businessDayIncrement.isDescriptionEditable||businessDayIncrement.isBooked">
                  <label
                    @click="businessDayIncrement.isDescriptionEditable=true;">
                    {{ businessDayIncrement.description }}
                  </label>
                </div>
                <input v-show="businessDayIncrement.isDescriptionEditable&&!businessDayIncrement.isBooked"
                       v-model="businessDayIncrement.description"
                       class="descriptionCell"
                       name="description"
                       v-on:blur="businessDayIncrement.isDescriptionEditable=false;"
                       @keydown.esc="businessDayIncrement.isDescriptionEditable=false"
                       @keyup.enter="businessDayIncrement.isDescriptionEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
                />
              </td>
              <td>
                <div v-show="!businessDayIncrement.isBeginTimeStampEditable||businessDayIncrement.isBooked">
                  <label
                    @click="businessDayIncrement.isBeginTimeStampEditable=true;">
                    {{ businessDayIncrement.timeSnippetDto.beginTimeStampRepresentation }}
                  </label>
                </div>
                <input v-show="businessDayIncrement.isBeginTimeStampEditable&&!businessDayIncrement.isBooked"
                       id="beginTimeStamp" v-model="businessDayIncrement.timeSnippetDto.beginTimeStampRepresentation"
                       name="beginTimeStamp"
                       type="time"
                       @keydown.esc="businessDayIncrement.isBeginTimeStampEditable=false"
                       @keyup.enter="businessDayIncrement.isBeginTimeStampEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
                />
              </td>
              <td>
                <div v-show="!businessDayIncrement.isEndTimeStampEditable||businessDayIncrement.isBooked">
                  <label
                    @click="businessDayIncrement.isEndTimeStampEditable=true;">
                    {{ businessDayIncrement.timeSnippetDto.endTimeStampRepresentation }}
                  </label>
                </div>
                <input v-show="businessDayIncrement.isEndTimeStampEditable&&!businessDayIncrement.isBooked" id="timeInput"
                       v-model="businessDayIncrement.timeSnippetDto.endTimeStampRepresentation"
                       name="beginTimeStamp"
                       type="time"
                       @keydown.esc="businessDayIncrement.isEndTimeStampEditable=false"
                       @keyup.enter="businessDayIncrement.isBeginTimeStampEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
                />
              </td>
              <td>
                <div v-show="!businessDayIncrement.isServiceCodeEditable||businessDayIncrement.isBooked"
                     class="service-code-cell">
                  <label
                    @click="businessDayIncrement.isServiceCodeEditable=true;">
                    {{ businessDayIncrement.serviceCodeDto.representation }}
                  </label>
                </div>
                <service-code-select
                  v-show="businessDayIncrement.isServiceCodeEditable&&!businessDayIncrement.isBooked"
                  v-model="businessDayIncrement.serviceCodeDto"
                  v-bind:initialServiceCode="businessDayIncrement.serviceCodeDto.value"
                  v-bind:initialServiceCodeRepresentation="businessDayIncrement.serviceCodeDto.representation"
                  v-bind:ticketNr="businessDayIncrement.ticketDto.ticketNr"
                  v-on:change="changeBusinessDayIncrementAndRefresh(businessDayIncrement);businessDayIncrement.isServiceCodeEditable=false"
                  @keydown.esc="businessDayIncrement.isServiceCodeEditable=false"
                  @keyup.enter="businessDayIncrement.isServiceCodeEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
                >
                </service-code-select>
              </td>
              <td class="centeredText">{{ businessDayIncrement.isBooked ? 'Ja' : 'Nein' }}</td>
              <td>
                <div style="display: flex">
                  <button
                    :disabled="businessDayIncrement.isBooked"
                    v-bind:class="{ deleteButton: !businessDayIncrement.isBooked, inactiveDeleteButton: businessDayIncrement.isBooked}"
                    v-on:click="deleteBusinessDayIncrementAndRefresh(businessDayIncrement)">
                  </button>
                </div>
              </td>
            </tr>
          </table>
        </div>
        <div v-if="this.hasNoContent" class="no-table-content">
          <div></div>
          <spam> Keine Daten </spam>
          <div></div>
        </div>
        <div class="bottom">
          <div>
            <label>Gesamt Anzahl Stunden: {{ businessDay.totalDurationRep }}h</label>
          </div>
          <button :disabled="isDeleteAllButtonDisabled" v-on:click="deleteAllAndRefresh">Alles löschen</button>
        </div>
        <error-box v-if="postErrorDetails">
          {{ postErrorDetails }}
        </error-box>
      </div>
    </div>
  </div>
</template>

<script>
import timeRecorderApi from '../mixins/TimeRecorderApi';
import businessDayApi from '../mixins/BusinessDayApi';
import appApi from '../mixins/AppApi';
import ServiceCodeSelect from '../components/ServiceCodeSelect.vue';
import TicketNrInputField from '../components/TicketNrInputField.vue';
import ErrorBox from '../components/ErrorBox.vue';

export default {
  name: 'ZeiterfassungOverview',
  mixins: [timeRecorderApi, businessDayApi, appApi],
  components: {
    'service-code-select': ServiceCodeSelect,
    'ticket-nr-input-field': TicketNrInputField,
    'error-box': ErrorBox
  },
  data() {
    return {
      title: 'Verwaltung der aufgezeichneten Stunden',
    }
  },
  computed: {
    isDeleteAllButtonDisabled: function () {
      return !this.businessDay.isDeleteAllPossible;
    },
    businessDay: function () {
      return this.$store.getters.businessDay
    },
    hasNoContent: function (){
      return !this.businessDay.totalDuration || this.businessDay.totalDuration <= 0;
    }
  },
  methods: {
    onTicketNrChanged: function (businessDayIncrement, newTicketNr) {
      businessDayIncrement.ticketDto.ticketNr = newTicketNr;
    },
    deleteBusinessDayIncrementAndRefresh: function (businessDayIncrement) {
      this.deleteBusinessDayIncrement(businessDayIncrement);
      this.requestUiRefresh();
    },
    deleteAllAndRefresh: function () {
      this.deleteAll();
      this.requestUiRefresh();
    },
    changeBusinessDayIncrementAndRefresh: function (businessDayIncrement) {
      this.changeBusinessDayIncrement(businessDayIncrement);
      this.requestUiRefresh();
    },
  },
  mounted() {
    this.fetchBusinessDayDto();
  }
}
</script>

<style scoped>

.zeiterfassung-overview {
  overflow-x: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.table {
  max-height: 380px;
  width: auto;
  overflow-y: auto;
  overflow-x: hidden;
  padding-bottom: 20px;
}

.bottom {
  padding-top: 10px;
}

.flex-center {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.service-code-cell {
  white-space: nowrap;
}

.ticket-nr-cell {
  white-space: nowrap;
}

.deleteButton {
  margin: auto;
  height: 2vh;
  width: 1vw;
  cursor: pointer;
  background: url("../assets/trash.png") transparent no-repeat center center;
  background-size: 90% 90%;
  border-radius: 5px;
  box-shadow: inset 0 3px 6px rgba(0,0,0,0.16), 0 4px 6px rgba(0,0,0,0.45);
}

.inactiveDeleteButton {
  margin: auto;
  height: 2vh;
  width: 1vw;
  background: url("../assets/trash.png") #9F9F9F no-repeat center center;
  background-size: 90% 90%;
  border-radius: 5px;
  box-shadow: inset 0 3px 6px rgba(0,0,0,0.16), 0 4px 6px rgba(0,0,0,0.45);
}

.isBookedRecord {
  background-color: #9f9f9f
}

.no-table-content {
  display: flex;
  padding: 30px;
  justify-content: space-between;
  color: #b6b6b6;
}
</style>
