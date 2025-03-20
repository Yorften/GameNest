import { useAppDispatch, useAppSelector } from '../../app/hooks';
import Button from '../../components/miscs/Button';
import { selectCurrentUser } from '../../features/auth/authSlice';

type Props = {}

export default function NewGame({ }: Props) {
  const dispatch = useAppDispatch();
  const user = useAppSelector(selectCurrentUser);
  return (
    <>
      <div className="flex items-center justify-between mb-4 px-4 pt-4">
        <h2 className="text-3xl font-semibold">Build a new game</h2>
      </div>
    </>
  )
}