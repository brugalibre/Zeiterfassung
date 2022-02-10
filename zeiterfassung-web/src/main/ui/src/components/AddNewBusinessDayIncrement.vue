<template>
  <div id="addBusinessDayIncrementForm" class="blurBackground">
    <div class="inputForm centered tile">
      <p>
        <ticket-nr-input-field
          v-model="businessDayIncrement.ticketNr"
          class="containerElement100"
          required
          v-bind:initTicketNr="businessDayIncrement.ticketNr"
          @ticketNrChanged="onTicketNrChanged"
        >
        </ticket-nr-input-field>
      </p>
      <div>
        <input
          id="description"
          v-model="businessDayIncrement.description"
          class="descriptionInput"
          name="description"
          placeholder="Beschreibung"
          type="text"
        >
      </div>
      <p>
        <service-code-select
          v-model="businessDayIncrement.serviceCodeDto"
          class="containerElement100"
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
          v-model="businessDayIncrement.beginTimeValue"
          name="beginTimeStamp"
          required
          v-bind:initTimeRepresentation="businessDayIncrement.beginTimeValue.timeRepresentation"
          @timeRepChanged="onBeginTimeRepChanged"
        >
        </time-input-field>
        <label for='endTimeStamp'>Bis: </label>
        <time-input-field
          id="endTimeStamp"
          v-model="businessDayIncrement.endTimeValue"
          name="endTimeStamp"
          required
          v-bind:initTimeRepresentation="businessDayIncrement.endTimeValue.timeRepresentation"
          @timeRepChanged="onEndTimeRepChanged"
        >
        </time-input-field>
      </p>
      <p>
        <label>Dauer: </label>
        <input
          id="duration"
          v-model="businessDayIncrement.currentDuration"
          class="timeInputField"
          name="duration"
          required
          type="number"
        />
        <label> h</label>
      </p>
      <p class="container containerElement100">
        <button :disabled="isSubmitButtonDisabled" class="container-element element-left"
                v-on:click="addBusinessDayIncrementAndRefresh(businessDayIncrement)">Speichern
        </button>
        <button class="container-element element-right" v-on:click="resume">Abbrechen</button>
      </p>
    </div>
    <error-box v-if="postErrorDetails">
      {{ postErrorDetails }}
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
  data() {
    return {
      businessDayIncrement: {
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
    'businessDayIncrement.currentDuration': function (newVal, oldVal) {
      console.log("Changed duration from old '" + oldVal + ", to new '" + newVal);
      var newEndTimeValue = this.calculateDateFromDuration(this.businessDayIncrement.beginTimeValue.timeValue, newVal);
      this.businessDayIncrement.endTimeValue.timeValue = newEndTimeValue.timeValue;
      this.businessDayIncrement.endTimeValue.timeRepresentation = newEndTimeValue.timeRepresentation;
    },
  },
  methods: {
    addBusinessDayIncrementAndRefresh: function (businessDayIncrement) {
      this.addBusinessDayIncrement(businessDayIncrement);
      setTimeout(() => this.requestUiRefresh(), 1000);
      console.log("AddNewBusinessDayInc: refresh Ui requested");
    },
    onBeginTimeRepChanged: function (beginTimeValue) {
      console.log("On-Begin-Time-Rep-Changed '" + beginTimeValue.timeValue + ", amount of Hours '" + beginTimeValue.timeValue.getHours() + "', amount of minutes '" + beginTimeValue.timeValue.getMinutes() + "'");
      this.businessDayIncrement.beginTimeValue = beginTimeValue;
      if (this.businessDayIncrement.endTimeValue.timeValue !== null) {
        this.businessDayIncrement.currentDuration = this.beginOrEndTimeStampChanged(beginTimeValue.timeValue, this.businessDayIncrement.endTimeValue.timeValue);
      }
    },
    onEndTimeRepChanged: function (endTimeValue) {
      console.log("On-End-Time-Rep-Changed '" + endTimeValue + ", amount of Hours '" + endTimeValue.timeValue.getHours() + "', amount of minutes '" + endTimeValue.timeValue.getMinutes() + "'");
      this.businessDayIncrement.endTimeValue = endTimeValue;
      if (this.businessDayIncrement.beginTimeValue.timeValue !== null) {
        this.businessDayIncrement.currentDuration = this.beginOrEndTimeStampChanged(this.businessDayIncrement.beginTimeValue.timeValue, endTimeValue.timeValue);
      }
    },
    onTicketNrChanged: function (newTicketNr) {
      this.businessDayIncrement.ticketNr = newTicketNr;
    },
  },
  computed: {
    isSubmitButtonDisabled: function () {
      return !this.businessDayIncrement.ticketNr || !this.businessDayIncrement.serviceCodeDto.value
        || !this.businessDayIncrement.endTimeValue.timeRepresentation || !this.businessDayIncrement.beginTimeValue.timeRepresentation
        || this.businessDayIncrement.currentDuration <= 0;
    },
  },
}
</script>
<style scoped>

.descriptionInput {
  width: 97%; /* don't ask me why but with a width of 100% the input field is slightly to long :S*/
}

.containerElement100 {
  width: 100%;
}

.timeInputField {
  width: 5vh;
}

.inputForm {
  background: white;
  position: fixed;
  width: 230px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
}

.blurBackground {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(5px);
  height: 100vh;
  width: 200vh;
  position: fixed;
  top: 0%;
  left: 0%;
  z-index: 1;
}
</style>
