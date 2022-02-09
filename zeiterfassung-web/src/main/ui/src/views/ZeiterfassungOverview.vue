<template>
  <div>
    <h2 class="centeredText">{{ title }}</h2>
    <div class="tableAndBottom">
      <table>
        <tr>
          <th id="amountOfHours">Anzahl Stunden</th>
          <th id="ticket" class="ticketNrCell">Ticket</th>
          <th id="description" class="descriptionCell">Beschreibung</th>
          <th id="from">Von</th>
          <th id="upto">Bis</th>
          <th id="servicecode" class="serviceCodeCell">Leistungsart</th>
          <th id="booked">Abgebucht</th>
          <th id="delete">Eintrag löschen</th>
        </tr>
        <tr v-for="businessDayIncrement in businessDay.businessDayIncrementDtos" :key="businessDayIncrement.id"
            v-bind:class="{ isBookedRecord: businessDayIncrement.isBooked}">
          <td>
            <div v-show="!businessDayIncrement.isDurationRepEditable||businessDayIncrement.isBooked">
              <label
                @click="businessDayIncrement.isDurationRepEditable=true;">{{ businessDayIncrement.timeSnippetDto.durationRep }} </label>
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
            <div v-show="!businessDayIncrement.isTicketNrEditable||businessDayIncrement.isBooked" class="ticketNrCell">
              <label
                @click="businessDayIncrement.isTicketNrEditable=true;">{{ businessDayIncrement.ticketDto.ticketNr }} </label>
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
                @click="businessDayIncrement.isDescriptionEditable=true;">{{ businessDayIncrement.description }} </label>
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
                @click="businessDayIncrement.isBeginTimeStampEditable=true;">{{ businessDayIncrement.timeSnippetDto.beginTimeStampRepresentation }} </label>
            </div>
            <input v-show="businessDayIncrement.isBeginTimeStampEditable&&!businessDayIncrement.isBooked" id="beginTimeStamp" v-model="businessDayIncrement.timeSnippetDto.beginTimeStampRepresentation"
                   name="beginTimeStamp"
                   type="time"
                   @keydown.esc="businessDayIncrement.isBeginTimeStampEditable=false"
                   @keyup.enter="businessDayIncrement.isBeginTimeStampEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
            />
          </td>
          <td>
            <div v-show="!businessDayIncrement.isEndTimeStampEditable||businessDayIncrement.isBooked">
              <label
                @click="businessDayIncrement.isEndTimeStampEditable=true;">{{ businessDayIncrement.timeSnippetDto.endTimeStampRepresentation }} </label>
            </div>
            <input v-show="businessDayIncrement.isEndTimeStampEditable&&!businessDayIncrement.isBooked" id="timeInput" v-model="businessDayIncrement.timeSnippetDto.endTimeStampRepresentation"
                   name="beginTimeStamp"
                   type="time"
                   @keydown.esc="businessDayIncrement.isEndTimeStampEditable=false"
                   @keyup.enter="businessDayIncrement.isBeginTimeStampEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
            />
          </td>
          <td>
            <div v-show="!businessDayIncrement.isServiceCodeEditable||businessDayIncrement.isBooked"
                 class="serviceCodeCell">
              <label
                @click="businessDayIncrement.isServiceCodeEditable=true;">{{ businessDayIncrement.serviceCodeDto.representation }} </label>
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
          <td>{{ businessDayIncrement.isBooked ? 'Ja' : 'Nein' }}</td>
          <td>
            <button
              :disabled="businessDayIncrement.isBooked"
              v-bind:class="{ deleteButton: !businessDayIncrement.isBooked, inactiveDeleteButton: businessDayIncrement.isBooked}"
              v-on:click="deleteBusinessDayIncrementAndRefresh(businessDayIncrement)">
            </button>
          </td>
        </tr>
      </table>
      <div><!--placeholder --></div>
      <div class="bottom">
        <div>
          <label>Gesamt Anzahl Stunden: {{ businessDay.totalDurationRep }}h</label>
        </div>
        <button :disabled="isDeleteAllButtonDisabled" v-on:click="deleteAllAndRefresh">Alles löschen</button>
      </div>
    </div>
    <error-box v-if="postErrorDetails">
      {{ postErrorDetails }}
    </error-box>
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
      title: 'Verwaltung der aufgezeichneten Stunden:',
    }
  },
  computed: {
    isDeleteAllButtonDisabled: function () {
      return !this.businessDay.isDeleteAllPossible;
    },
    businessDay: function () {
      return this.$store.getters.businessDay
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

.tableAndBottom {
  display: flex;
  flex-direction: column;

  justify-content: space-between;
  overflow: auto;
  height: 375px;
  width: auto;
  padding-right: 5px;
}

.serviceCodeCell {
  white-space: nowrap;
}

.ticketNrCell {
  white-space: nowrap;
}

.deleteButton {
  padding: 0;
  margin: 0;
  height: 4vh;
  width: 4vh;
  cursor: pointer;
  background: url("../assets/white_trash.png") #358fe6 no-repeat center center;
  background-size: 200% 125%;
}

.inactiveDeleteButton {
  padding: 0;
  margin: 0;
  height: 4vh;
  width: 4vh;
  background: url("../assets/white_trash.png") lightslategray no-repeat center center;
  background-size: 200% 125%;
}

.isBookedRecord {
  background-color: lightslategray
}

.bottom {
  padding-bottom: 5px;
}
</style>
