<template>
  <div id=addBusinessDayIncrementForm class="blurBackground">
    <div class="inputForm container0 border">
      <p>
        <ticket-nr-input-field
          class="containerElement100"
          required
          v-model="businessDayIncrement.ticketNr"
          v-bind:initTicketNr="businessDayIncrement.ticketNr"
          @ticketNrChanged="onTicketNrChanged"
        >
        </ticket-nr-input-field>
      </p>
      <div>
        <input
          class="descriptionInput"
          id="description"
          v-model="businessDayIncrement.description"
          type="text"
          name="description"
          placeholder="Beschreibung"
        >
      </div>
      <p>
        <service-code-select
          class="containerElement100"
          v-model="businessDayIncrement.serviceCodeDto"
          v-bind:initialServiceCode="businessDayIncrement.serviceCodeDto.value"
          v-bind:initialServiceCodeRepresentation="businessDayIncrement.serviceCodeDto.representation"
          v-bind:ticketNr="businessDayIncrement.ticketNr"
        >
        </service-code-select>
      </p>
      <p class="container">
        <label for="von">Von: </label>
        <input
          id="beginTimeStamp"
          required
          v-model="businessDayIncrement.beginTimeStampRepresentation"
          type="time"
          name="beginTimeStamp"
        />
        <label for="upto">Bis: </label>
        <input
          id="endTimeStamp"
          required
          v-model="businessDayIncrement.endTimeStampRepresentation"
          type="time"
          name="endTimeStamp"
        />
      </p>
      <p>
        <label>Dauer: </label>
        <input
          id="duration"
          type="number"
          required
          v-model="businessDayIncrement.currentDuration"
          name="duration"
        />
        <label> h</label>
      </p>
      <p class="container containerElement100">
        <button class="containerElement" :disabled="isSubmitButtonDisabled" v-on:click="addBusinessDayIncrement(businessDayIncrement)">Speichern</button>
        <button class="containerElement" v-on:click="resume">Abbrechen</button>
      </p>
    </div>
	</div>
</template>
<script>
import timeRecorderApi from '../mixins/TimeRecorderApi';
import businessDayApi from '../mixins/BusinessDayApi';
import dateCalculation from '../mixins/DateCalculation';
import ServiceCodeSelect from './ServiceCodeSelect.vue';
import TicketNrInputField from './TicketNrInputField.vue';

export default {
  name: 'AddNewBusinessDayIncrement',
  mixins: [timeRecorderApi, businessDayApi, dateCalculation],
  components: {
    'service-code-select': ServiceCodeSelect,
    'ticket-nr-input-field': TicketNrInputField
  },
  props: ['initBeginTimeStampRepresentation', 'initEndTimeStampRepresentation'],
  data (){
    return {
      businessDayIncrement : {
        currentDuration: '',
        ticketNr: 'SYRIUS',
        description: '',
        serviceCodeDto: {
          value: '',
          representation: '',
        },
        beginTimeStampRepresentation: this.initBeginTimeStampRepresentation,
        endTimeStampRepresentation: this.initEndTimeStampRepresentation,
        possibleServiceCodes: '',
      }
    }
  },
  watch: {
    'businessDayIncrement.currentDuration': function (newVal, oldVal){
      console.log("Changed duration from old '" + oldVal + ", to new '" + newVal);
      this.businessDayIncrement.endTimeStampRepresentation = this.calculateDateFromDuration(this.businessDayIncrement.beginTimeStampRepresentation,
        this.businessDayIncrement.endTimeStampRepresentation, newVal);
    },
    'businessDayIncrement.beginTimeStampRepresentation': function (newVal, oldVal){
      console.log("Changed begin-time from old '" + oldVal + ", to new '" + newVal);
      this.businessDayIncrement.currentDuration = this.beginOrEndTimeStampChanged(newVal,
        this.businessDayIncrement.endTimeStampRepresentation, this.businessDayIncrement.currentDuration);
    },
    'businessDayIncrement.endTimeStampRepresentation': function (newVal, oldVal){
      console.log("Changed end-time from old '" + oldVal + ", to new '" + newVal);
      this.businessDayIncrement.currentDuration = this.beginOrEndTimeStampChanged(this.businessDayIncrement.beginTimeStampRepresentation,
        newVal, this.businessDayIncrement.currentDuration );
    },
  },
  methods: {
    onTicketNrChanged: function(newTicketNr){
      this.businessDayIncrement.ticketNr = newTicketNr;
    },
  },
  computed: {
    isSubmitButtonDisabled: function() {
      return !this.businessDayIncrement.ticketNr || !this.businessDayIncrement.serviceCodeDto.value
        || !this.businessDayIncrement.beginTimeStampRepresentation || !this.businessDayIncrement.endTimeStampRepresentation;
    },
  },
}
</script>

<style scoped>

  .descriptionInput{
    width: 97%;/* don't ask me why but with a width of 100% the input field is slightly to long :S*/
  }

  .border{
    top: 0;
    left: 0;
    border: 10px;
    border-width: 10px;
    border-color: red

  }

  .inputForm{
    position: fixed;
    top: 50%;
    left: 35%;
    transform: translate(-50%, -50%);
  }

  .blurBackground{
    /* background: red; */
    background: rgba(255, 255, 255, 0.4);
    backdrop-filter: blur(8px);
    height: 100vh;
    width: 250vh;
    position: fixed;
    top: 0%;
    left: 0%;
  }
</style>
