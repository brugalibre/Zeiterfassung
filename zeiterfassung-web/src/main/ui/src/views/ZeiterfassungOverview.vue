<template>
  <div>
    <h2>{{ title }}</h2>
    <table>
      <tr>
        <th id="amountOfHours">Anzahl Stunden</th>
        <th id="ticket" class="ticketNrCell">Ticket</th>
        <th id="description">Beschreibung</th>
        <th id="from">Von</th>
        <th id="upto">Bis</th>
        <th id="servicecode" class="serviceCodeCell">Leistungsart</th>
        <th id="booked">Abgebucht</th>
        <th id="delete">Eintrag löschen</th>
      </tr>
      <tr v-for="businessDayIncrement in businessDay.businessDayIncrementDtos" :key="businessDayIncrement.id" v-bind:class="{ isBookedRecord: businessDayIncrement.isBooked}" >
        <td>
          <div v-show="!businessDayIncrement.isDurationRepEditable">
            <label @click="businessDayIncrement.isDurationRepEditable=true;">{{businessDayIncrement.timeSnippetDto.durationRep}} </label>
          </div>
            <input name="durationRep"
              v-show="businessDayIncrement.isDurationRepEditable"
              v-model="businessDayIncrement.timeSnippetDto.durationRep"
              v-on:blur= "businessDayIncrement.isDurationRepEditable=false;"
              @keydown.esc="businessDayIncrement.isDurationRepEditable=false"
              @keyup.enter="businessDayIncrement.isDurationRepEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
            />
        </td>
        <td>
          <div class="ticketNrCell" v-show="!businessDayIncrement.isTicketNrEditable">
            <label @click="businessDayIncrement.isTicketNrEditable=true;">{{businessDayIncrement.ticketDto.ticketNr}} </label>
          </div>
          <ticket-nr-input-field
            v-show="businessDayIncrement.isTicketNrEditable"
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
        <div v-show="!businessDayIncrement.isDescriptionEditable">
          <label @click="businessDayIncrement.isDescriptionEditable=true;">{{businessDayIncrement.description}} </label>
        </div>
          <input name="description"
            v-show="businessDayIncrement.isDescriptionEditable"
            v-model="businessDayIncrement.description"
            v-on:blur="businessDayIncrement.isDescriptionEditable=false;"
            @keydown.esc="businessDayIncrement.isDescriptionEditable=false"
            @keyup.enter="businessDayIncrement.isDescriptionEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
          />
        </td>
        <td>
          <div v-show="!businessDayIncrement.isBeginTimeStampEditable">
          <label @click="businessDayIncrement.isBeginTimeStampEditable=true;">{{businessDayIncrement.timeSnippetDto.beginTimeStampRepresentation}} </label>
        </div>
          <input type="time" id="beginTimeStamp" name="beginTimeStamp"
            v-show="businessDayIncrement.isBeginTimeStampEditable"
            v-model="businessDayIncrement.timeSnippetDto.beginTimeStampRepresentation"
            @keydown.esc="businessDayIncrement.isBeginTimeStampEditable=false"
            @keyup.enter="businessDayIncrement.isBeginTimeStampEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
            />
        </td>
        <td>
          <div v-show="!businessDayIncrement.isEndTimeStampEditable">
            <label @click="businessDayIncrement.isEndTimeStampEditable=true;">{{businessDayIncrement.timeSnippetDto.endTimeStampRepresentation}} </label>
          </div>
          <input type="time" id="beginTimeStamp" name="beginTimeStamp"
            v-show="businessDayIncrement.isEndTimeStampEditable"
            v-model="businessDayIncrement.timeSnippetDto.endTimeStampRepresentation"
            @keydown.esc="businessDayIncrement.isEndTimeStampEditable=false"
            @keyup.enter="businessDayIncrement.isBeginTimeStampEditable=false;changeBusinessDayIncrementAndRefresh(businessDayIncrement)"
            />
        </td>
        <td>
          <div class="serviceCodeCell" v-show="!businessDayIncrement.isServiceCodeEditable">
            <label @click="businessDayIncrement.isServiceCodeEditable=true;">{{businessDayIncrement.serviceCodeDto.representation}} </label>
          </div>
          <service-code-select
            v-show="businessDayIncrement.isServiceCodeEditable"
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
        <td><button class="deleteButton" v-on:click="deleteBusinessDayIncrementAndRefresh(businessDayIncrement)">
          <img src='../assets/white_trash.png'></button></td>
      </tr>
    </table>
    <p>Gesamt Anzahl Stunden: {{businessDay.totalDurationRep}}h</p>
    <div >
      <button :disabled="isDeleteAllButtonDisabled" v-on:click="deleteAllAndRefresh">Alles löschen</button>
    </div>
    <error-box v-if="postErrorDetails">
      {{postErrorDetails}}
    </error-box>
</div>
</template>

<script>
import timeRecorderApi from '../mixins/TimeRecorderApi';
import businessDayApi from '../mixins/BusinessDayApi';
import ServiceCodeSelect from '../components/ServiceCodeSelect.vue';
import TicketNrInputField from '../components/TicketNrInputField.vue';
import ErrorBox from '../components/ErrorBox.vue';

export default {
  name: 'ZeiterfassungOverview',
  mixins: [timeRecorderApi, businessDayApi],
  components: {
    'service-code-select': ServiceCodeSelect,
    'ticket-nr-input-field': TicketNrInputField,
    'error-box': ErrorBox
  },
  data() {
    return {
      title: 'Verwaltung der bisher aufgezeichneten Stunden:',
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
      this.$emit('refreshUi', false);
    },
    deleteAllAndRefresh: function(){
      this.deleteAll();
      this.$emit('refreshUi', false);
    },
    changeBusinessDayIncrementAndRefresh: function(businessDayIncrement){
      this.changeBusinessDayIncrement(businessDayIncrement);
      this.$emit('refreshUi', false);
    },
  },
  mounted() {
    this.fetchBusinessDayDto();
  }
}
</script>

<style scoped>

  .serviceCodeCell{
    width: 225px;
  }
  .ticketNrCell{
    width: 125px;
  }

  .deleteButton img{
    width: 150%;
    height: 150%;
    left:50%;
    right:50%;
    transform: translate(-15%, 0%);
    padding: 0px 0px;
    margin: 0px;
    padding:0px;
  }
  .deleteButton {
    margin: 0px;
    padding:0px;
    height: 75%;
    width: 75%;
  }

  .isBookedRecord{
    background-color:lightslategray
  }

</style>
