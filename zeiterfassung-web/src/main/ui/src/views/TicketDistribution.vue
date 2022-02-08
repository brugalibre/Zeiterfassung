<template>
  <div>
    <h2 class="centeredText">{{ title }}</h2>
    <pie-chart
      class="centered"
      empty="Keine Daten"
      loading="Lade Daten..."
      id="ticket-distribution-pie-chart"
      legend="bottom"
      width="230px" height="230px"
      suffix="%"
      :round="2"
      :data="ticketDistributionChartData"
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
