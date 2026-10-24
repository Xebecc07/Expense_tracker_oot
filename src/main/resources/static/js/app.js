// Bottom sheet for "Add expense" + PWA service worker registration.
(function () {
  var fab = document.getElementById('fab'), sheet = document.getElementById('sheet'), overlay = document.getElementById('overlay');
  if (fab && sheet && overlay) {
    var toggle = function (open) { sheet.classList.toggle('open', open); overlay.classList.toggle('open', open); };
    fab.addEventListener('click', function () { toggle(true); });
    overlay.addEventListener('click', function () { toggle(false); });
  }
  if ('serviceWorker' in navigator) navigator.serviceWorker.register('/sw.js').catch(function () {});
})();
