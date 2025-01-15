export const Popup = ({ text, closePopup }) => {
  return (
    <div className="popup-container">
      <div className="popup-body">
        <h1>
          {text.split('\n').map((line, index) => (
            <span key={index}>
              {line}
              {index < text.split('\n').length - 1 && <br />}
            </span>
          ))}
        </h1>
        <button className="pretty-button" onClick={closePopup}>Close X</button>
      </div>
    </div>
  );
};