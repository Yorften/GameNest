import Button from '../../components/miscs/Button'
import { Link, useNavigate } from 'react-router'
import { RxExternalLink } from "react-icons/rx";
import { SlOptions } from "react-icons/sl";
import { useAppDispatch, useAppSelector } from '../../app/hooks';
import { fetchGames, Game, selectAllGames, setSelectedGame } from '../../features/games/gameSlice';
import { useEffect } from 'react';

type Props = {}

export default function UserGames({ }: Props) {
  const dispatch = useAppDispatch()
  const navigate = useNavigate()

  const games = useAppSelector(selectAllGames);

  useEffect(() => {
    dispatch(fetchGames())
  }, [dispatch])

  function handleClick(game: Game): void {
    // dispatch(setSelectedGame(game))
    navigate(`/dashboard/games/${game.id}`)
  }

  return (
    <>
      <div className='flex flex-col gap-8'>
        <div className="flex items-center justify-between mb-4 px-4">
          <h2 className="text-3xl font-semibold">My Games</h2>
          <Button onClick={() => navigate("new")} className='!text-sm font-medium !bg-gray-50/10 border border-primary' title="New Game"></Button>
        </div>
        <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 place-items-center gap-8 px-4'>
          {games.map((game, index) => (
            <div key={index} onClick={() => handleClick(game)} className='flex flex-col justify-between max-h-40 min-h-40 h-full w-full border border-primary rounded-md cursor-pointer p-6'>
              <div className='flex w-full justify-between'>
                <div className='flex items-center gap-4'>
                  <img src="/assets/images/godot_icon.png" className='w-8 h-8 object-cover' alt="Godot icon" />
                  <div className='text-sm text-white/80'>
                    <Link className='block hover:underline' to={`${game.id}`}>{game.title}</Link>
                    <Link className='text-xs hover:underline' to={`/games/${game.id}`} target='_blank'>{window.location.origin} <RxExternalLink className='inline-block' />
                    </Link>
                  </div>
                </div>
                <SlOptions />
              </div>
              <div>
                <p className='text-sm'>{game.description.length > 36
                  ? `${game.description.slice(0, 36)}...`
                  : game.description}</p>
              </div>
              <div className='flex items-center justify-between'>
                <p className='text-sm'>{game.lastBuild?.updatedAt
                  ? `Last build on ${new Date((game.lastBuild.updatedAt)!.toLocaleString()).toLocaleString()}`
                  : "No builds yet"
                }</p>
                <div className='bg-gray-50/10 rounded-xl shadow-lg text-xs font-medium border border-primary px-3'>
                  <p>{game.category?.name}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  )
}
