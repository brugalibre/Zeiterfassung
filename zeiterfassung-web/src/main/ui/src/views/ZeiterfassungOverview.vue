<template>
  <div class="zeiterfassungOverviewContainer">
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
        <tr v-for="businessDayIncrement in businessDay.businessDayIncrementDtos" :key="businessDayIncrement.id" v-bind:class="{ isBookedRecord: businessDayIncrement.isBooked}" >
          <td>
            <div v-show="!businessDayIncrement.isDurationRepEditable||businessDayIncrement.isBooked">
              <label @click="businessDayIncrement.isDurationRepEditable=true;">{{businessDayIncrement.timeSnippetDto.durationRep}} </label>
            </div>
              <input name="durationRep"
                v-show="businessDayIncrement.isDurationRepEditable&&!businessDayIncrement.isBooked"
                v-model="businessDayIncrement.timeSnippetDto.durationRep"
                v-on:blur= "businessDayIncrement.isDurationRepEditable=false;"
                @keydown.esc="businessDayIncrement.isDurationRepEditable=false"
                @keyup.enter="businessDayIncrement.isDurationRepEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
              />
          </td>
          <td>
            <div class="ticketNrCell" v-show="!businessDayIncrement.isTicketNrEditable||businessDayIncrement.isBooked">
              <label @click="businessDayIncrement.isTicketNrEditable=true;">{{businessDayIncrement.ticketDto.ticketNr}} </label>
            </div>
            <ticket-nr-input-field
              v-show="businessDayIncrement.isTicketNrEditable&&!businessDayIncrement.isBooked"
              v-model="businessDayIncrement.ticketDto.ticketNr"
              v-bind:initTicketNr="businessDayIncrement.ticketDto.ticketNr"
              v-on:blur="businessDayIncrement.isTicketNrEditable=false"
              @change="businessDayIncrement.isTicketNrEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
              @keydown.esc="businessDayIncrement.isTicketNrEditable=false"
              @keyup.enter="businessDayIncrement.isTicketNrEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
              @ticketNrChanged="onTicketNrChanged(businessDayIncrement, $event)"
            >
            </ticket-nr-input-field>
          </td>
          <td class="editableTableCell">
          <div v-show="!businessDayIncrement.isDescriptionEditable||businessDayIncrement.isBooked">
            <label @click="businessDayIncrement.isDescriptionEditable=true;">{{businessDayIncrement.description}} </label>
          </div>
            <input name="description"
              class="descriptionCell"
              v-show="businessDayIncrement.isDescriptionEditable&&!businessDayIncrement.isBooked"
              v-model="businessDayIncrement.description"
              v-on:blur="businessDayIncrement.isDescriptionEditable=false;"
              @keydown.esc="businessDayIncrement.isDescriptionEditable=false"
              @keyup.enter="businessDayIncrement.isDescriptionEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
            />
          </td>
          <td>
            <div v-show="!businessDayIncrement.isBeginTimeStampEditable||businessDayIncrement.isBooked">
            <label @click="businessDayIncrement.isBeginTimeStampEditable=true;">{{businessDayIncrement.timeSnippetDto.beginTimeStampRepresentation}} </label>
          </div>
            <input type="time" id="beginTimeStamp" name="beginTimeStamp"
              v-show="businessDayIncrement.isBeginTimeStampEditable&&!businessDayIncrement.isBooked"
              v-model="businessDayIncrement.timeSnippetDto.beginTimeStampRepresentation"
              @keydown.esc="businessDayIncrement.isBeginTimeStampEditable=false"
              @keyup.enter="businessDayIncrement.isBeginTimeStampEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
              />
          </td>
          <td>
            <div v-show="!businessDayIncrement.isEndTimeStampEditable||businessDayIncrement.isBooked">
              <label @click="businessDayIncrement.isEndTimeStampEditable=true;">{{businessDayIncrement.timeSnippetDto.endTimeStampRepresentation}} </label>
            </div>
            <input type="time" id="timeInput" name="beginTimeStamp"
              v-show="businessDayIncrement.isEndTimeStampEditable&&!businessDayIncrement.isBooked"
              v-model="businessDayIncrement.timeSnippetDto.endTimeStampRepresentation"
              @keydown.esc="businessDayIncrement.isEndTimeStampEditable=false"
              @keyup.enter="businessDayIncrement.isBeginTimeStampEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
              />
          </td>
          <td>
            <div class="serviceCodeCell" v-show="!businessDayIncrement.isServiceCodeEditable||businessDayIncrement.isBooked">
              <label @click="businessDayIncrement.isServiceCodeEditable=true;">{{businessDayIncrement.serviceCodeDto.representation}} </label>
            </div>
            <service-code-select
              v-show="businessDayIncrement.isServiceCodeEditable&&!businessDayIncrement.isBooked"
              v-bind:ticketNr="businessDayIncrement.ticketDto.ticketNr"
              v-bind:initialServiceCode="businessDayIncrement.serviceCodeDto.value"
              v-bind:initialServiceCodeRepresentation="businessDayIncrement.serviceCodeDto.representation"
              v-on:change="changeBusinessDayIncrementAndRefresh(businessDayIncrement);businessDayIncrement.isServiceCodeEditable=false"
              @keydown.esc="businessDayIncrement.isServiceCodeEditable=false"
              @keyup.enter="businessDayIncrement.isServiceCodeEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
              v-model="businessDayIncrement.serviceCodeDto"
            >
            </service-code-select>
          </td>
          <td>{{businessDayIncrement.isBooked ? 'Ja' : 'Nein'}}</td>
          <td>
            <button
              v-bind:class="{ deleteButton: !businessDayIncrement.isBooked, inactiveDeleteButton: businessDayIncrement.isBooked}"
              :disabled="businessDayIncrement.isBooked"
              v-on:click="deleteBusinessDayIncrementAndRefresh(businessDayIncrement)">
            </button>
          </td>
        </tr>
      </table>
      <div class="bottom">
        <div>
          <label>Gesamt Anzahl Stunden: {{businessDay.totalDurationRep}}h</label>
        </div>
        <button :disabled="isDeleteAllButtonDisabled" v-on:click="deleteAllAndRefresh">Alles löschen</button>
      </div>
    </div>
    <error-box v-if="postErrorDetails">
      {{postErrorDetails}}
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
    isDeleteAllButtonDisabled: function(){
      return !this.businessDay.isDeleteAllPossible;
    },
  },
  methods: {
    onTicketNrChanged: function(businessDayIncrement, newTicketNr){
      businessDayIncrement.ticketDto.ticketNr = newTicketNr;
    },
    deleteBusinessDayIncrementAndRefresh: function(businessDayIncrement){
      this.deleteBusinessDayIncrement(businessDayIncrement);
      this.requestUiRefresh();
    },
    deleteAllAndRefresh: function(){
      this.deleteAll();
      this.requestUiRefresh();
    },
    changeBusinessDayIncrementAndRefresh: function(businessDayIncrement){
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

  .zeiterfassungOverviewContainer{
    position: relative;
  }

  .tableAndBottom{
     overflow: auto;
     height: 375px;
     width: auto;
     padding-right: 5px;
  }

  .serviceCodeCell{
    white-space: nowrap;
  }

  .ticketNrCell{
    white-space: nowrap;
  }

  .deleteButton{
    padding: 0;
    margin: 0;
    height: 4vh;
    width: 4vh;
    cursor:pointer;
    background: url("../assets/white_trash.png") #358fe6 no-repeat center center;
    background-size: 200% 125%;
  }

  .inactiveDeleteButton{
    padding: 0;
    margin: 0;
    height: 4vh;
    width: 4vh;
    background: url("../assets/white_trash.png") lightslategray no-repeat center center;
    background-size: 200% 125%;
  }

  .isBookedRecord{
    background-color:lightslategray
  }

  .bottom{
    position: absolute;
    bottom: 0;
    padding-bottom: 5px;
  }
</style>
