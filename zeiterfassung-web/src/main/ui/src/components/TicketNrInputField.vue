<template>
  <div class="ticktNrInputAndSelect">
    <input
      id="ticktNrInput"
      v-model="ticketNr"
      class="ticktNrInput"
      name="ticketNr"
      placeholder="Ticket-Nr."
      type="text"
      @input="ticketNrChanged($event)"
    >
    <select
      v-model="ticketNr"
      class="ticktNrSelect"
      @change="ticketNrChanged($event)"
      @keyup.enter="ticketNrChanged($event)">
      <option
        v-for="ticket in tickets" :key="ticket"
        v-bind:value="ticket.ticketNr"> {{ ticket.ticketRepresentation }}
      </option>
    </select>
  </div>
</template>

<script>
import ticketBacklogApi from '../mixins/TicketBacklogApi';

export default {
  name: 'TicketNrInputField',
  props: ['initTicketNr'],
  emits: ['ticketNrChanged'],
  mixins: [ticketBacklogApi],
  data() {
    return {
      ticketNr: this.initTicketNr,
    }
  },
  methods: {
    ticketNrChanged: function (event) {
      this.$emit('ticketNrChanged', event.target.value);
    }
  },
  mounted() {
    this.fetchTicketsFromBacklog();
  },
}
</script>

<style>
.ticktNrInputAndSelect {
  align-items: stretch;
  display: flex; /* equal height of the children */
  width: 100%;
}

.ticktNrInput {
  width: 90%;
}

.ticktNrSelect {
  width: 10%;
}

</style>
