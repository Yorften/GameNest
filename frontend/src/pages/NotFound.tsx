import { useNavigate } from 'react-router-dom';
import Button from '../components/miscs/Button';

const NotFound = () => {
  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate(-1);
  };

  return (
    <div className="flex items-center justify-center h-[50vh] w-full">
      <div className="flex flex-col items-center gap-6">
        <p className=" text-white/80 text-5xl font font-medium">Not Found 404</p>

        <Button
          title="Go Back"       
          onClick={handleGoBack}
          type="button"       
        />
      </div>
    </div>
  );
};

export default NotFound;