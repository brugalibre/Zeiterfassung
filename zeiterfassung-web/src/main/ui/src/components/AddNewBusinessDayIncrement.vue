<template>
  <div id="addBusinessDayIncrementForm" class="blurBackground">
    <div class="inputForm centered tile">
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
        <time-input-field
          id="beginTimeStamp"
          @timeRepChanged="onBeginTimeRepChanged"
          required
          v-model="businessDayIncrement.beginTimeValue"
          v-bind:initTimeRepresentation="businessDayIncrement.beginTimeValue.timeRepresentation"
          name="beginTimeStamp"
        >
        </time-input-field>
        <label for="upto">Bis: </label>
        <time-input-field
          id="endTimeStamp"
          @timeRepChanged="onEndTimeRepChanged"
          required
          v-model="businessDayIncrement.endTimeValue"
          v-bind:initTimeRepresentation="businessDayIncrement.endTimeValue.timeRepresentation"
          name="endTimeStamp"
        >
        </time-input-field>
      </p>
      <p>
        <label>Dauer: </label>
        <input
          class="timeInputField"
          id="duration"
          type="number"
          required
          v-model="businessDayIncrement.currentDuration"
          name="duration"
        />
        <label> h</label>
      </p>
      <p class="container containerElement100">
        <button class="containerElement" :disabled="isSubmitButtonDisabled" v-on:click="addBusinessDayIncrementAndRefresh(businessDayIncrement)">Speichern</button>
        <button class="containerElement" v-on:click="resume">Abbrechen</button>
      </p>
    </div>
    <error-box v-if="postErrorDetails">
      {{postErrorDetails}}
    </error-box>
	</div>
</template>
<script>
import timeRecorderApi from '../mixins/TimeRecorderApi';
import businessDayApi from '../mixins/BusinessDayApi';
import dateCalculation from '../mixins/DateCalculation';
import appApi from '../mixins/AppApi';

import ErrorBox from './ErrorBox.vue';
import ServiceCodeSelect from './ServiceCodeSelect.vue';
import TimeInputField from './TimeInputField.vue';
import TicketNrInputField from './TicketNrInputField.vue';

export default {
  name: 'AddNewBusinessDayIncrement',
  mixins: [timeRecorderApi, businessDayApi, dateCalculation, appApi],
  components: {
    'service-code-select': ServiceCodeSelect,
    'time-input-field': TimeInputField,
    'ticket-nr-input-field': TicketNrInputField,
    'error-box': ErrorBox
  },
  props: ['initBeginTimeStampRepresentation', 'initEndTimeStampRepresentation', 'initTicketNr'],
  data () {
    return {
      businessDayIncrement : {
        currentDuration: 0,
        ticketNr: this.initTicketNr,
        description: '',
        serviceCodeDto: {
          value: '',
          representation: '',
        },
        beginTimeValue: {
          timeRepresentation: this.initBeginTimeStampRepresentation,
          timeValue: null,
        },
        endTimeValue: {
          timeRepresentation: this.initEndTimeStampRepresentation,
          timeValue: null,
        },
        possibleServiceCodes: '',
      }
    }
  },
  watch: {
    'businessDayIncrement.currentDuration': function(newVal, oldVal){
      console.log("Changed duration from old '" + oldVal + ", to new '" + newVal);
      var newEndTimeValue = this.calculateDateFromDuration(this.businessDayIncrement.beginTimeValue.timeValue, newVal);
      this.businessDayIncrement.endTimeValue.timeValue = newEndTimeValue.timeValue;
      this.businessDayIncrement.endTimeValue.timeRepresentation = newEndTimeValue.timeRepresentation;
    },
  },
  methods: {
    addBusinessDayIncrementAndRefresh: function(businessDayIncrement){
      this.addBusinessDayIncrement(businessDayIncrement);
      setTimeout(() => this.requestUiRefresh(), 1000);
      console.log("AddNewBusinessDayInc: refresh Ui requested");
    },
    onBeginTimeRepChanged: function(beginTimeValue){
      console.log("On-Begin-Time-Rep-Changed '" + beginTimeValue.timeValue + ", amount of Hours '" + beginTimeValue.timeValue.getHours() + "', amount of minutes '" + beginTimeValue.timeValue.getMinutes() +"'");
      this.businessDayIncrement.beginTimeValue = beginTimeValue;
      if (this.businessDayIncrement.endTimeValue.timeValue !== null){
        this.businessDayIncrement.currentDuration = this.beginOrEndTimeStampChanged (beginTimeValue.timeValue, this.businessDayIncrement.endTimeValue.timeValue);
      }
    },
    onEndTimeRepChanged: function(endTimeValue){
      console.log("On-End-Time-Rep-Changed '" + endTimeValue + ", amount of Hours '" + endTimeValue.timeValue.getHours()+ "', amount of minutes '" + endTimeValue.timeValue.getMinutes() +"'");
      this.businessDayIncrement.endTimeValue = endTimeValue;
      if (this.businessDayIncrement.beginTimeValue.timeValue !== null){
        this.businessDayIncrement.currentDuration = this.beginOrEndTimeStampChanged(this.businessDayIncrement.beginTimeValue.timeValue, endTimeValue.timeValue);
      }
    },
    onTicketNrChanged: function(newTicketNr){
      this.businessDayIncrement.ticketNr = newTicketNr;
    },
  },
  computed: {
    isSubmitButtonDisabled: function() {
      return !this.businessDayIncrement.ticketNr || !this.businessDayIncrement.serviceCodeDto.value
        || !this.businessDayIncrement.endTimeValue.timeRepresentation || !this.businessDayIncrement.beginTimeValue.timeRepresentation
        || this.businessDayIncrement.currentDuration <= 0;
    },
  },
}
</script>
<style scoped>
  .container0{
    display: inline-block;
    justify-content: space-between;
  }

  .descriptionInput{
    width: 97%;/* don't ask me why but with a width of 100% the input field is slightly to long :S*/
  }

  .containerElement100{
    width: 100%;
  }

  .timeInputField{
    width: 5vh;
  }

  .inputForm{
    background: white;
    position: fixed;
    max-width: 15%;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 2;
  }

  .blurBackground{
    background: rgba(255, 255, 255, 0.6);
    backdrop-filter: blur(5px);
    height: 100vh;
    width: 250vh;
    position: fixed;
    top: 0%;
    left: 0%;
    z-index: 1;
  }
</style>
