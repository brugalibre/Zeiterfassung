<template>
  <div>
    <h2 class="centeredText">{{ title }}</h2>
    <pie-chart
      id="ticket-distribution-pie-chart"
      :data="ticketDistributionChartData"
      :round="2"
      class="centered"
      empty="Keine Daten"
      height="230px" legend="bottom"
      loading="Lade Daten..."
      suffix="%"
      width="230px"
    >
    </pie-chart>
  </div>
</template>

<script>

export default {
  name: 'TicketDistribution',
  computed: {
    ticketDistributionChartData() {
      return this.$store.getters.ticketDistributionChartData;
    }
  },
  data() {
    return {
      title: 'Monatsübersicht Projekte',
    }
  }
}
</script>
