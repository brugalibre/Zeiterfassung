<template>
  <select 
    v-model="serviceCodeDto">
    <option 
      v-for="possibleServiceCode in possibleServiceCodes" :key="possibleServiceCode"
      v-bind:value="possibleServiceCode"> {{ possibleServiceCode.representation }}
    </option>
  </select>
</template>

<script>
import ticketBacklogApi from '../mixins/TicketBacklogApi';
export default {
  props: ['ticketNr', 'initialServiceCode', 'initialServiceCodeRepresentation'],
  name: 'ServiceCodeSelect',
  mixins: [ticketBacklogApi],
  data (){
    return {
      serviceCodeDto: {
        value: this.initialServiceCode,
        representation: this.initialServiceCodeRepresentation,
      },
      possibleServiceCodes: '',
    }
  },
  watch: {
    ticketNr: {
      handler: function(newTicketNr, oldTicketNr) {
        console.log("Changed ticket from  " + oldTicketNr + " to " + newTicketNr);
        this.fetchServiceCodes(this.newTicketNr);
      },
    },
  },
  mounted() {
    this.fetchServiceCodes(this.ticketNr);
  },
}
</script>
