import 'bootstrap';
import flatpickr from 'flatpickr';
import 'scss/app.scss';

document.querySelectorAll('.js-submit-confirm').forEach(($item) => {
    $item.addEventListener('submit', (event) => {
        if (!confirm(event.currentTarget.getAttribute('data-confirm-message'))) {
            event.preventDefault();
            return false;
        }
        return true;
    });
});

document.querySelectorAll('.js-datepicker, .js-timepicker, .js-datetimepicker').forEach(($item) => {
    const flatpickrConfig = {
        allowInput: true,
        time_24hr: true,
        enableSeconds: true
    };
    if ($item.classList.contains('js-datepicker')) {
        flatpickrConfig.dateFormat = 'Y-m-d';
    } else if ($item.classList.contains('js-timepicker')) {
        flatpickrConfig.enableTime = true;
        flatpickrConfig.noCalendar = true;
        flatpickrConfig.dateFormat = 'H:i:S';
    } else { // datetimepicker
        flatpickrConfig.enableTime = true;
        flatpickrConfig.altInput = true;
        flatpickrConfig.altFormat = 'Y-m-d H:i:S';
        flatpickrConfig.dateFormat = 'Y-m-dTH:i:S';
    }
    flatpickr($item, flatpickrConfig);
});
